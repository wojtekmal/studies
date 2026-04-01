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
} Element;

typedef struct Vector
{
    uint64_t allocated;
    uint64_t size;
    Element* data;
} Vector;

typedef struct rstack_t
{
    Vector elements;
    uint64_t reference_count;
    uint64_t last_dfs_id;
} rstack_t;

int push_node(Vector* vector, IntOrStack value, bool is_stack)
{
    if (vector->size == vector->allocated)
    {
        uint64_t new_allocated = vector->size * 2 + 1;
        vector->allocated = new_allocated;
        vector->data = realloc(vector->data, new_allocated * sizeof(Element));

        if (vector->data == nullptr)
        {
            errno = ENOMEM;
            return -1;
        }
    }

    vector->data[vector->size].contents = value;
    vector->data[vector->size].is_stack = is_stack;
    vector->size++;

    return 0;
}

Vector new_vector()
{
    Vector result;
    result.allocated = 0;
    result.size = 0;
    result.data = malloc(0);

    if (result.data == nullptr) errno = ENOMEM;
    return result;
}

void free_vector(Vector* vector)
{
    free(vector->data);
}

rstack_t* rstack_new()
{
    rstack_t* result = malloc(sizeof(rstack_t));

    if (result == nullptr)
    {
        errno = ENOMEM;
    }
    else
    {
        result->elements = new_vector();
        result->reference_count = 1;
    }

    return result;
}

void rstack_delete(rstack_t *rs)
{
    if (rs == nullptr) return;

    rs->reference_count--;
    if (rs->reference_count == 0) free_vector(&(rs->elements));
}

int rstack_push_value(rstack_t *rs, uint64_t value)
{
    if (rs == nullptr)
    {
        errno = EINVAL;
        return -1;
    }

    return push_node(&(rs->elements), (IntOrStack){.num = value}, false);
}

int rstack_push_rstack(rstack_t *rs1, rstack_t *rs2)
{
    if (rs1 == nullptr || rs2 == nullptr)
    {
        errno = EINVAL;
        return -1;
    }

    rs2->reference_count++;
    return push_node(&(rs1->elements), (IntOrStack){.stack = rs2}, true);
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