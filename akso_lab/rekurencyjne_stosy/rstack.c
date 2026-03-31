#include "rstack.h"

typedef union IntOrStack
{
    rstack_t* stack;
    uint64_t num;
} IntOrStack;

typedef struct Element
{
    IntOrStack contents;
    bool type; // 0 if int, 1 if stack.
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
} rstack_t;

void push_node(Vector* vector, IntOrStack value, bool type)
{
    if (vector->size == vector->allocated)
    {
        int new_allocated = vector->size * 2 + 1;
        vector->data = realloc(vector->data, new_allocated * sizeof(Element));
        vector->allocated = new_allocated;
    }

    vector->data[vector->size].contents = value;
    vector->data[vector->size].type = type;
    vector->size++;
}

Vector new_vector()
{
    Vector result;
    result.allocated = 0;
    result.size = 0;
    result.data = nullptr;
    return result;
}

void stack_dfs(Vector* result, Vector* traversed_stacks)

Vector rstack_get_all_ints(rstack_t* rs)
{
    Vector result = new_vector();
    Vector traversed_stacks = new_vector();
    stack_dfs(&result, &traversed_stacks);
    return result;
}