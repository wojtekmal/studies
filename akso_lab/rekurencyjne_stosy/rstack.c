#include "rstack.h"
#include <errno.h>
#include <stdio.h>
#include <sys/types.h>
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
    uintmax_t reference_count;
    uintmax_t last_dfs_id;

    uintmax_t dfs_where_was_part_of_scc;
    uintmax_t scc_reference_count;

    struct rstack* prev_in_write_dfs;
};

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
        result->reference_count = 1;
        result->last_dfs_id = 0;
        result->dfs_where_was_part_of_scc = 0;
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
    
    rs2->reference_count++;
    rs1->top = element;
    return 0;
}

bool measure_and_flag_scc_dfs(rstack_t* rs, uintmax_t* scc_size,
    uintmax_t dfs_id)
{
    if (rs->last_dfs_id != dfs_id)
    {
        rs->last_dfs_id = dfs_id;
        bool found_path_to_start = false;

        for (Element* element = rs->top; element != nullptr;
            element = element->prev_element)
        {
            if (element->is_stack == false) continue;

            if (measure_and_flag_scc_dfs(element->stack, scc_size,
                dfs_id)) found_path_to_start = true;
        }

        // Second condition is a check for the start, to not double-count.
        if (found_path_to_start && rs->dfs_where_was_part_of_scc != dfs_id)
        {
            ++*scc_size;
            rs->dfs_where_was_part_of_scc = dfs_id;
        }
    }

    return (rs->dfs_where_was_part_of_scc == dfs_id);
}

void count_ready_in_scc_dfs(rstack_t* rs, uintmax_t* ready_count,
    uintmax_t dfs_id)
{
    if (rs->dfs_where_was_part_of_scc != dfs_id - 1) return;

    if (rs->last_dfs_id != dfs_id)
    {
        rs->last_dfs_id = dfs_id;
        rs->scc_reference_count = 0;

        for (Element* element = rs->top; element != nullptr;
            element = element->prev_element)
        {
            if (element->is_stack == false) continue;

            count_ready_in_scc_dfs(element->stack, ready_count, dfs_id);
        }
    }

    rs->scc_reference_count++;
    if (rs->scc_reference_count == rs->reference_count) ++*ready_count;
}

void gather_for_pruning_dfs(rstack_t* rs, rstack_t** next_to_prune,
    uintmax_t dfs_id)
{
    if (rs->dfs_where_was_part_of_scc != dfs_id - 2 ||
        rs->last_dfs_id == dfs_id) return;

    rs->last_dfs_id = dfs_id;

    for (Element* element = rs->top; element != nullptr;
        element = element->prev_element)
    {
        if (element->is_stack == false) continue;

        gather_for_pruning_dfs(element->stack, next_to_prune, dfs_id);
    }

    // Top of the stack is used to point to the next stack for removal.
    if (rs->top->is_stack) rs->top->stack->reference_count--;
    rs->top->stack = *next_to_prune;
    *next_to_prune = rs;
}

void prune_elements_recursively(Element* to_prune)
{
    if (to_prune == nullptr) return;

    prune_elements_recursively(to_prune->prev_element);

    if (to_prune->is_stack)
    {
        to_prune->stack->reference_count--;
    }

    free(to_prune);
}

void prune_stacks_recursively(rstack_t* to_prune)
{
    if (to_prune == nullptr) return;

    prune_stacks_recursively(to_prune->top->stack);

    prune_elements_recursively(to_prune->top->prev_element);

    free(to_prune->top);
    free(to_prune);
}

void rstack_delete(rstack_t *rs)
{
    if (rs == nullptr) return;

    uintmax_t scc_size = 1;
    rs->dfs_where_was_part_of_scc = next_dfs_id;
    measure_and_flag_scc_dfs(rs, &scc_size, next_dfs_id);
    next_dfs_id++;

    uintmax_t ready_count = 0;
    count_ready_in_scc_dfs(rs, &ready_count, next_dfs_id++);

    // Has to be after count_ready_in_scc_dfs because that function increases
    // the scc incoming edge counter when it enters.
    rs->reference_count--;

    if (ready_count != scc_size) return;

    if (rs->top == nullptr)
    {
        free(rs);
        return;
    }

    rstack_t* next_to_prune = nullptr;
    gather_for_pruning_dfs(rs, &next_to_prune, next_dfs_id++);

    prune_stacks_recursively(next_to_prune);
}

void rstack_pop(rstack_t *rs)
{
    if (rs == nullptr || rs->top == nullptr) return;

    Element* element = rs->top;
    if (element->is_stack) rstack_delete(element->stack);
    rs->top = element->prev_element;
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

rstack_t* rstack_read(char const *path)
{
    if (path == nullptr)
    {
        errno = EINVAL;
        return nullptr;
    }

    int file_descriptor = open(path, O_RDONLY);
    if (file_descriptor == -1) return nullptr; // open sets errno.

    struct stat file_statistics;
    int fstat_result = fstat(file_descriptor, &file_statistics);
    if (fstat_result == -1) return nullptr; // fstat sets errno.
    uintmax_t file_size = file_statistics.st_size;

    char* buffer = malloc(file_size + 1);
    if (buffer == nullptr)
    {
        errno = ENOMEM;
        return nullptr;
    }
    buffer[file_size] = 0x0;

    int read_result = read(file_descriptor, buffer, file_size);
    if (read_result == -1) return nullptr; // read sets errno.

    for (uintmax_t i = 0; i < file_size; i++)
    {
        if (!isspace(buffer[i]) && !isdigit(buffer[i]))
        {
            errno = EINVAL;
            return nullptr;
        }
    }

    // Discard all following whitespace.
    for (uintmax_t i = file_size - 1; i + 1 != 0 && isspace(buffer[i]); i--)
    {
        buffer[i] = 0x0;
    }

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

    int close_result = close(file_descriptor);
    if (close_result == -1) return nullptr; // close sets errno.

    return result;
}

int write_dfs(rstack_t* rs, Element* element, FILE* file, bool* loop_found,
    rstack_t* prev_in_write_dfs)
{
    if (element == nullptr) return 0;

    if (element == rs->top)
    {
        rs->prev_in_write_dfs = prev_in_write_dfs;

        for (rstack_t* on_path = prev_in_write_dfs; on_path != nullptr;
            on_path = on_path->prev_in_write_dfs)
        {
            if (on_path != rs) continue;

            *loop_found = true;
            return 0;
        }
    }

    int prev_result =
        write_dfs(rs, element->prev_element, file, loop_found, nullptr);
    if (prev_result == -1 || *loop_found) return prev_result;

    if (element->is_stack)
    {
        return write_dfs(
            element->stack, element->stack->top, file, loop_found, rs);
    }
    else
    {
        int fprintf_result = fprintf(file, "%ju\n", element->num);
        return (fprintf_result < 0) ? -1 : 0; // errno set by fprintf.
    }
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
    int write_dfs_result = write_dfs(rs, rs->top, file, &loop_found, nullptr);
    if (write_dfs_result == -1) return -1;

    int fclose_output = fclose(file);
    if (fclose_output != 0) return -1; // fclose sets errno.

    return 0;
}