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
} rstack_t;

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

    rs2->reference_count++;
    
}

void rstack_delete(rstack_t *rs)
{
    if (rs == nullptr) return;

    rs->reference_count--;
    if (rs->reference_count == 0) free_vector(&(rs->elements));
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