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
} rstack_t;

void push_node(Vector* vector, IntOrStack value, bool is_stack)
{
    if (vector->size == vector->allocated)
    {
        uint64_t new_allocated = vector->size * 2 + 1;
        vector->data = realloc(vector->data, new_allocated * sizeof(Element));
        vector->allocated = new_allocated;
    }

    vector->data[vector->size].contents = value;
    vector->data[vector->size].is_stack = is_stack;
    vector->size++;
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

void get_ints_dfs(rstack_t* rs, Vector* result, Vector* traversed_stacks)
{
    for (uint64_t i = 0; i < traversed_stacks->size; i++)
    {
        if (traversed_stacks->data[i].contents.stack == rs) return;
    }

    push_node(traversed_stacks, (IntOrStack) rs, true);

    for (uint64_t i = 0; i < rs->elements.size; i++)
    {
        Element element = rs->elements.data[i];

        if (element.is_stack == false)
        {
            push_node(result, (IntOrStack) element.contents, false);
        }
        else
        {
            get_ints_dfs(element.contents.stack, result, traversed_stacks);
        }
    }
}

void free_vector(Vector* vector)
{
    free(vector->data);
}

Vector rstack_get_all_ints(rstack_t* rs)
{
    Vector result = new_vector();
    Vector traversed_stacks = new_vector();

    get_ints_dfs(rs, &result, &traversed_stacks);
    free_vector(&traversed_stacks);
    return result;
}

rstack_t* rstack_new()
{

}