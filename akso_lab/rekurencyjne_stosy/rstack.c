#include "rstack.h"

typedef struct Node
{
    rstack_t* stack;
    uint64_t num;
    bool type;
} Node;

typedef struct Vector
{
    uint64_t allocated;
    uint64_t size;
    Node* data;
} Vector;

typedef struct rstack_t
{
    Vector elements;
    uint64_t reference_count;
} rstack_t;

