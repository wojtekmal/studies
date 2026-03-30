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

