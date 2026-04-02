#include "rstack.h"
#include "errno.h"

typedef union IntOrStack
{
    rstack_t* stack;
    uint64_t num;
} IntOrStack;

typedef struct Element
{
    IntOrStack contents;
    bool is_stack; // false if int, true if stack.
    Element* prev_element;
} Element;

typedef struct rstack_t
{
    Element* top;
    uint64_t reference_count;
    uint64_t last_dfs_id;

    uint64_t dfs_where_was_part_of_scc;
    uint64_t scc_reference_count;
} rstack_t;

uint64_t next_dfs_id = 1;

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

    element->contents.num = value;
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

    element->contents.stack = rs2;
    element->is_stack = true;
    element->prev_element = rs1->top;
    
    rs2->reference_count++;
    rs1->top = element;
    return 0;
}

bool measure_and_flag_scc_dfs(rstack_t* rs, uint64_t* scc_size, uint64_t dfs_id)
{
    if (rs->last_dfs_id == dfs_id)
    {
        return (rs->dfs_where_was_part_of_scc == dfs_id);
    }
    else
    {
        rs->last_dfs_id = dfs_id;
        bool is_in_scc = false;

        for (Element* element = rs->top; element != nullptr;
            element = element->prev_element)
        {
            if (element->is_stack == false) continue;

            if (measure_and_flag_scc_dfs(element->contents.stack, scc_size,
                dfs_id)) is_in_scc = true;
        }

        if (is_in_scc)
        {
            *scc_size++;
            rs->dfs_where_was_part_of_scc = dfs_id;
        }

        return is_in_scc;
    }
}

void count_ready_in_scc_dfs(rstack_t* rs, uint64_t* ready_count,
    uint64_t dfs_id)
{
    if (rs->dfs_where_was_part_of_scc != dfs_id - 1) return;

    if (rs->last_dfs_id != dfs_id)
    {
        rs->last_dfs_id = dfs_id;
        rs->scc_reference_count = 1;

        for (Element* element = rs->top; element != nullptr;
            element = element->prev_element)
        {
            if (element->is_stack == false) continue;

            count_ready_dfs(element->contents.stack, ready_count, dfs_id);
        }
    }
    else
    {
        rs->reference_count++;
    }

    if (rs->scc_reference_count == rs->reference_count) *ready_count++;
}

void gather_for_pruning_dfs(rstack_t* rs, uint64_t dfs_id,
    rstack_t** next_to_prune)
{
    if (rs->dfs_where_was_part_of_scc != dfs_id - 2) return;
    if (rs->last_dfs_id == dfs_id) return;

    rs->last_dfs_id = dfs_id;

    for (Element* element = rs->top; element != nullptr;
        element = element->prev_element)
    {
        if (element->is_stack == false) continue;

        count_ready_dfs(element->contents.stack, dfs_id, next_to_prune);
    }

    rs->top->contents.stack = *next_to_prune;
    *next_to_prune = rs;
}

void rstack_delete(rstack_t *rs)
{
    if (rs == nullptr) return;

    uint64_t scc_size = 0;
    rs->last_dfs_id = next_dfs_id;
    rs->dfs_where_was_part_of_scc = next_dfs_id;
    bool _ = measure_and_flag_scc_dfs(rs, &scc_size, next_dfs_id);
    next_dfs_id++;

    uint64_t ready_count = 0;
    count_ready_in_scc_dfs(rs, &ready_count, next_dfs_id);
    next_dfs_id++;

    if (ready_count != scc_size) return;

    if (rs->top == nullptr)
    {
        free(rs);
        return;
    }

    rstack_t* next_to_prune = nullptr;
    gather_for_pruning_dfs(rs, next_dfs_id, &next_to_prune);

    prune_stacks_recursively(next_to_prune);
}

void rstack_pop(rstack_t *rs)
{
    if (rs == nullptr || rs->elements.size == 0) return;

    Element* element = rs->elements.data + rs->elements.size - 1;

    if (element->is_stack) element->contents.stack->reference_count--;
    rs->elements.size--;
}

result_t rstack_front(rstack_t *rs)
{
    if (rs == nullptr) return (result_t){.flag = false};

    for ()
}

bool rstack_empty(rstack_t *rs)
{
    Vector all_ints = rstack_get_all_ints(rs);
    return ()
}