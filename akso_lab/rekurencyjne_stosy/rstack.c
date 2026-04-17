#include "rstack.h"
#include <errno.h>
#include <stdio.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/stat.h>
#include <ctype.h>

typedef struct Element
{
    union
    {
        rstack_t* stack;
        uint64_t num;
    };
    bool is_stack; // false if int, true if stack.
    struct Element* prev_element;
} Element;

struct rstack
{
    Element* top;
    uintmax_t last_dfs_id;

    // Stacks that were deleted are "dead", the rest are alive. Deletion works 
    // by removing the stack from the list of alive stacks and running mark and
    // sweep.
    bool mark;
    struct rstack* prev_alive;
    struct rstack* next_alive;

    struct rstack* prev_in_write_dfs;
};

// Handle for the list of alive stacks.
rstack_t* last_alive = nullptr;

// Used by a few dfs functions to remember which stacks were visited.
uintmax_t next_dfs_id = 1;

rstack_t* rstack_new()
{
    rstack_t* result = malloc(sizeof(rstack_t));

    if (result == nullptr)
    {
        errno = ENOMEM;
    }
    else
    {
        result->top = nullptr;
        result->last_dfs_id = 0;

        // The stack is appended to the list of alive stacks. Three pointers
        // need to be set: the connection before the new and last stack 
        // (both ways) and nullptr at the end. 
        result->prev_alive = last_alive;
        result->next_alive = nullptr;
        
        if (last_alive != nullptr) last_alive->next_alive = result;

        last_alive = result;
    }

    return result;
}

int rstack_push_value(rstack_t *rs, uint64_t value)
{
    if (rs == nullptr)
    {
        errno = EINVAL;
        return -1;
    }

    Element* element = malloc(sizeof(Element));

    if (element == nullptr)
    {
        errno = ENOMEM;
        return -1;
    }

    element->num = value;
    element->is_stack = false;
    element->prev_element = rs->top;

    rs->top = element;
    return 0;
}

int rstack_push_rstack(rstack_t *rs1, rstack_t *rs2)
{
    if (rs1 == nullptr || rs2 == nullptr)
    {
        errno = EINVAL;
        return -1;
    }

    Element* element = malloc(sizeof(Element));

    if (element == nullptr)
    {
        errno = ENOMEM;
        return -1;
    }

    element->stack = rs2;
    element->is_stack = true;
    element->prev_element = rs1->top;
    
    rs1->top = element;
    return 0;
}

void remove_from_alive_list(rstack_t *rs)
{
    if (rs == last_alive) last_alive = rs->prev_alive;

    if (rs->prev_alive != nullptr)
    {
        rs->prev_alive->next_alive = rs->next_alive;
    }

    if (rs->next_alive != nullptr)
    {
        rs->next_alive->prev_alive = rs->prev_alive;
    }
}

void set_mark_dfs(rstack_t *rs, bool mark_value, uintmax_t dfs_id)
{
    // Used both for clearing the marks and for setting them.
    if (rs->last_dfs_id == dfs_id) return;

    rs->last_dfs_id = dfs_id;
    rs->mark = mark_value;

    for (Element* element = rs->top; element != nullptr;
        element = element->prev_element)
    {
        if (!element->is_stack) continue;

        set_mark_dfs(element->stack, mark_value, dfs_id);
    }
}

bool prepare_for_sweep_dfs(rstack_t *rs, uintmax_t dfs_id)
{
    // Returns true if the current caller of this function should also call
    // sweep_dfs for this stack.
    if (rs->last_dfs_id == dfs_id || rs->mark == false) return false;

    rs->last_dfs_id = dfs_id;

    for (Element* element = rs->top; element != nullptr;
        element = element->prev_element)
    {
        if (!element->is_stack) continue;

        if (!prepare_for_sweep_dfs(element->stack, dfs_id))
        {
            // We remove unnecessary connections by treating the elements as
            // values, not pointers to stacks.
            element->is_stack = false;
        }
    }

    return true;
}

void sweep_dfs(rstack_t *rs)
{
    while (rs->top != nullptr)
    {
        Element *element = rs->top;

        if (element->is_stack)
        {
            sweep_dfs(element->stack);
        }

        rs->top = element->prev_element;
        free(element);
    }

    free(rs);
}

void mark_and_sweep(rstack_t *rs)
{
    // Observation: the existence of a path between rs and a given stack is a
    // necessary condition for the removal of the given stack.
    set_mark_dfs(rs, true, next_dfs_id++);

    for (rstack_t *alive_stack = last_alive; alive_stack != nullptr;
        alive_stack = alive_stack->prev_alive)
    {
        set_mark_dfs(alive_stack, false, next_dfs_id);
    }
    next_dfs_id++;

    // Orders the stacks for removal, so that sweep_dfs is called for each stack
    // only once. It's also why sweep_dfs doesn't need a dfs_id, because
    // prepare_for_sweep_dfs leaves only one path to each stack.
    // prepare_for_sweep_dfs returns true if sweep should be called in the given
    // stack by the caller of the prepare function. This means that we should
    // also check for this here, in case rs shouldn't be removed.
    if (prepare_for_sweep_dfs(rs, next_dfs_id++))
    {
        sweep_dfs(rs);
    }
}

void rstack_delete(rstack_t *rs)
{
    if (rs == nullptr) return;

    remove_from_alive_list(rs);

    mark_and_sweep(rs);
}

void rstack_pop(rstack_t *rs)
{
    if (rs == nullptr || rs->top == nullptr) return;

    Element* element = rs->top;

    // Order is important - we don't want a connection between rs and
    // element->stack in mark_and_sweep.
    rs->top = element->prev_element;

    if (element->is_stack) mark_and_sweep(element->stack);

    free(element);
}

result_t get_front_dfs(rstack_t* rs, uintmax_t dfs_id)
{
    if (rs->last_dfs_id == dfs_id) return (result_t){.flag = false};

    rs->last_dfs_id = dfs_id;

    for (Element* element = rs->top; element != nullptr;
        element = element->prev_element)
    {
        if (element->is_stack == false)
        {
            return (result_t){.flag = true, .value = element->num};
        }
        else
        {
            result_t result = get_front_dfs(element->stack, dfs_id);

            if (result.flag == true) return result;
        }
    }

    return (result_t){.flag = false};
}

result_t rstack_front(rstack_t *rs)
{
    if (rs == nullptr) return (result_t){.flag = false};

    return get_front_dfs(rs, next_dfs_id++);
}

bool rstack_empty(rstack_t *rs)
{
    return !rstack_front(rs).flag;
}

int check_and_trim_buffer(char* buffer, uintmax_t *buffer_size)
{
    // Last byte of buffer is a null byte.
    for (uintmax_t i = 0; i < *buffer_size - 1; i++)
    {
        if (!isspace(buffer[i]) && !isdigit(buffer[i]))
        {
            errno = EINVAL;
            return -1;
        }
    }

    // Discard all following whitespace.
    while (*buffer_size != 1 && isspace(buffer[*buffer_size - 2]))
    {
        --*buffer_size;
    }

    buffer[*buffer_size - 1] = 0x0;
    return 0;
}

int read_into_buffer(char const *path, char **buffer, uintmax_t *buffer_size)
{
    int file_descriptor = open(path, O_RDONLY);
    if (file_descriptor == -1) return -1; // open sets errno.

    struct stat file_statistics;
    int fstat_result = fstat(file_descriptor, &file_statistics);
    if (fstat_result == -1) return -1; // fstat sets errno.
    *buffer_size = file_statistics.st_size + 1;

    *buffer = malloc(*buffer_size);
    if (*buffer == nullptr)
    {
        errno = ENOMEM;
        return -1;
    }

    int read_result = read(file_descriptor, *buffer, *buffer_size - 1);
    if (read_result == -1) return -1; // read sets errno.

    int close_result = close(file_descriptor);
    if (close_result == -1) return -1; // close sets errno.

    int check_and_trim_result = check_and_trim_buffer(*buffer, buffer_size);
    return check_and_trim_result;
}

rstack_t* extract_stack_from_buffer(char* buffer)
{
    rstack_t* result = rstack_new();
    if (result == nullptr) return nullptr;

    char *scan_pos = buffer;

    while (true)
    {
        char *end_of_scan;
        unsigned long long value = strtoull(scan_pos, &end_of_scan, 10);

        if (scan_pos == end_of_scan) break;
        scan_pos = end_of_scan;

        // strtoull sets errno. Also take care of case where unsigned long long
        // holds more than 64 bits.
        if (value > UINT64_MAX) errno = ERANGE;
        if (errno == ERANGE) return nullptr;

        int push_result = rstack_push_value(result, value);
        if (push_result == -1) return nullptr;
    }

    free(buffer);
    return result;
}

rstack_t* rstack_read(char const *path)
{
    if (path == nullptr)
    {
        errno = EINVAL;
        return nullptr;
    }

    char* buffer;
    uintmax_t buffer_size;
    read_into_buffer(path, &buffer, &buffer_size);

    return extract_stack_from_buffer(buffer);
}

int write_dfs_stack(rstack_t* rs, FILE* file, bool* loop_found,
    rstack_t* prev_in_write_dfs);

int write_dfs_element(Element* element, FILE* file, bool* loop_found,
    rstack_t* prev_in_write_dfs)
{
    if (element == nullptr) return 0;

    int prev_result = write_dfs_element(
        element->prev_element, file, loop_found, prev_in_write_dfs);
    if (prev_result == -1 || *loop_found) return prev_result;

    if (element->is_stack)
    {
        return write_dfs_stack(
            element->stack, file, loop_found, prev_in_write_dfs);
    }
    else
    {
        int fprintf_result = fprintf(file, "%ju\n", element->num);
        return (fprintf_result < 0) ? -1 : 0; // errno set by fprintf.
    }
}

int write_dfs_stack(rstack_t* rs, FILE* file, bool* loop_found,
    rstack_t* prev_in_write_dfs)
{
    rs->prev_in_write_dfs = prev_in_write_dfs;

    for (rstack_t* on_path = prev_in_write_dfs; on_path != nullptr;
        on_path = on_path->prev_in_write_dfs)
    {
        if (on_path != rs) continue;

        *loop_found = true;
        return 0;
    }

    int write_element_result = write_dfs_element(rs->top, file, loop_found, rs);
    return write_element_result;
}

int rstack_write(char const *path, rstack_t *rs)
{
    if (path == nullptr || rs == nullptr)
    {
        errno = EINVAL;
        return -1;
    }

    FILE* file = fopen(path, "w");
    if (file == nullptr) return -1; // fopen sets errno.

    bool loop_found = false;
    int write_dfs_result = write_dfs_stack(rs, file, &loop_found, nullptr);
    if (write_dfs_result == -1) return -1;

    int fclose_output = fclose(file);
    if (fclose_output != 0) return -1; // fclose sets errno.

    return 0;
}