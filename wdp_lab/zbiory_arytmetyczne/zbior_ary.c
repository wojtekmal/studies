#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Walloc-size"
#define max(a, b) (a > b ? a : b)
#define min(a, b) (a < b ? a : b)

#include "zbior_ary.h"
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <stdio.h>

int global_q;

uint64_t get_hash(int a)
{
    uint64_t x = (uint64_t) ((a % global_q + global_q) % global_q);
    // splitmix64
    // This is a bijection, so 64 bit hashes aren't needed.
    x += 0x9E3779B97F4A7C15ULL;
    x = (x ^ (x >> 30)) * 0xBF58476D1CE4E5B9ULL;
    x = (x ^ (x >> 27)) * 0x94D049BB133111EBULL;
    x = x ^ (x >> 31);
    return x;
}

Node* add_node(zbior_ary* arithmetic_set, uint64_t hash)
{
    // Add a node to zbior_ary's BST and return the pointer. The keys (hashes)
    // are random, so there is no need for rebalancing.

    Node** current_node = &arithmetic_set->tree;

    while (true)
    {
        if (*current_node == 0)
        {
            *current_node = malloc(sizeof(Node));
            (*current_node)->real_size = 0;
            (*current_node)->allocated_size = 0;
            (*current_node)->segments = malloc(0);
            (*current_node)->hash = hash;
            (*current_node)->left = 0; // nullptr
            (*current_node)->right = 0; // nullptr
            return *current_node;
        }
        else if ((*current_node)->hash == hash)
        {
            return *current_node;
        }
        else if ((*current_node)->hash > hash)
        {
            current_node = &(*current_node)->left;
        }
        else
        {
            current_node = &(*current_node)->right;
        }
    }
}

zbior_ary constructor()
{
    zbior_ary result;
    result.tree = 0; // nullptr
    return result;
}

void resize_segments(Node* node, uint64_t requested_size)
{
    // A Node's segment array behaves like a std::vector<Segment>.

    uint64_t old_allocated_size = node->allocated_size;
    if (old_allocated_size >= requested_size) return;

    uint64_t new_allocated_size = max(old_allocated_size * 2 + 1, requested_size);
    uint64_t old_real_size = node->real_size;
    Segment* new_segment_array = malloc(new_allocated_size * sizeof(Segment));
    memcpy(new_segment_array, node->segments, old_real_size * sizeof(Segment));
    free(node->segments);
    
    node->segments = new_segment_array;
    node->allocated_size = new_allocated_size;
}

void add_segment(Node* node, Segment segment)
{
    // Behaves like node.push_back(segment), if node were a std::vector<Segment>.

    uint64_t old_size = node->real_size;
    resize_segments(node, old_size + 1);
    node->segments[old_size] = segment;
    node->real_size++;
}

zbior_ary ciag_arytmetyczny(int a, int q, int b)
{
    global_q = q;
    zbior_ary result = constructor();

    uint64_t hash = get_hash(a);
    Node* node = add_node(&result, hash);

    Segment segment = {a, b + global_q};
    add_segment(node, segment);
    return result;
}

zbior_ary singleton(int a)
{
    return ciag_arytmetyczny(a, global_q, a);
}

void flatten(Node* node, Node** begin, uint64_t* size)
{
    // Flatten a BST into a sorted array.

    if (!node)
    {
        *begin = malloc(0);
        *size = 0;
        return;
    }

    Node* left_begin;
    uint64_t left_size;
    flatten(node->left, &left_begin, &left_size);

    Node* right_begin;
    uint64_t right_size;
    flatten(node->right, &right_begin, &right_size);

    *size = left_size + 1ULL + right_size;
    *begin = malloc((*size) * sizeof(Node));
    
    memcpy(*begin, left_begin, left_size * sizeof(Node));
    free(left_begin);

    memcpy(*begin + left_size, node, sizeof(Node));

    memcpy(*begin + left_size + 1, right_begin, right_size * sizeof(Node));
    free(right_begin);
}

void paste_node(zbior_ary* arithmetic_set, const Node* node)
{
    // Used by suma, iloczyn and roznica to copy nodes into zbior_ary result.
    
    uint64_t hash = node->hash;
    Node* new_node = add_node(arithmetic_set, hash);
    new_node->segments = malloc(node->real_size * sizeof(Segment));
    memcpy(new_node->segments, node->segments, node->real_size * sizeof(Segment));

    new_node->real_size = node->real_size;
    new_node->allocated_size = node->real_size;
}

zbior_ary suma(zbior_ary A, zbior_ary B)
{
    zbior_ary result = constructor();

    Node* a_flattened_begin;
    uint64_t a_flattened_size;
    flatten(A.tree, &a_flattened_begin, &a_flattened_size);
    Node* a_flattened_end = a_flattened_begin + a_flattened_size;
    Node* a_node = a_flattened_begin;

    Node* b_flattened_begin;
    uint64_t b_flattened_size;
    flatten(B.tree, &b_flattened_begin, &b_flattened_size);
    Node* b_flattened_end = b_flattened_begin + b_flattened_size;
    Node* b_node = b_flattened_begin;

    while (a_node < a_flattened_end || b_node < b_flattened_end)
    {
        uint64_t a_hash = a_node < a_flattened_end ? a_node->hash : ULLONG_MAX;
        uint64_t b_hash = b_node < b_flattened_end ? b_node->hash : ULLONG_MAX;

        if (a_hash < b_hash)
        {
            paste_node(&result, a_node);
            a_node++;
            continue;
        }
        if (a_hash > b_hash)
        {
            paste_node(&result, b_node);
            b_node++;
            continue;
        }

        uint64_t hash = a_node->hash;

        uint64_t a_size = a_node->real_size;
        uint64_t b_size = b_node->real_size;

        Node* result_node = add_node(&result, hash);

        uint64_t a_index = 0, b_index = 0;

        Segment current_segment = {INT_MIN, INT_MIN};
        Segment past_the_end = {INT_MAX, INT_MAX};

        while (a_index < a_size || b_index < b_size)
        {
            Segment a_segment = a_index < a_size ? a_node->segments[a_index] : past_the_end;
            Segment b_segment = b_index < b_size ? b_node->segments[b_index] : past_the_end;
            Segment new_segment;
            
            if (a_segment.begin < b_segment.begin)
            {
                new_segment = a_segment;
                a_index++;
            }
            else
            {
                new_segment = b_segment;
                b_index++;
            }

            if (current_segment.end < new_segment.begin)
            {
                if (current_segment.end != INT_MIN)
                {
                    add_segment(result_node, current_segment);
                }

                current_segment = new_segment;
            }
            else
            {
                current_segment.end = new_segment.end;
            }
        }

        if (current_segment.begin < current_segment.end)
        {
            add_segment(result_node, current_segment);
        }

        a_node++;
        b_node++;
    }

    return result;
}

zbior_ary iloczyn(zbior_ary A, zbior_ary B)
{
    zbior_ary result = constructor();

    Node* a_flattened_begin;
    uint64_t a_flattened_size;
    flatten(A.tree, &a_flattened_begin, &a_flattened_size);
    Node* a_flattened_end = a_flattened_begin + a_flattened_size;
    Node* a_node = a_flattened_begin;

    Node* b_flattened_begin;
    uint64_t b_flattened_size;
    flatten(B.tree, &b_flattened_begin, &b_flattened_size);
    Node* b_flattened_end = b_flattened_begin + b_flattened_size;
    Node* b_node = b_flattened_begin;

    while (a_node < a_flattened_end || b_node < b_flattened_end)
    {
        uint64_t a_hash = a_node < a_flattened_end ? a_node->hash : ULLONG_MAX;
        uint64_t b_hash = b_node < b_flattened_end ? b_node->hash : ULLONG_MAX;

        if (a_hash < b_hash)
        {
            a_node++;
            continue;
        }
        if (a_hash > b_hash)
        {
            b_node++;
            continue;
        }

        uint64_t hash = a_node->hash;

        uint64_t a_size = a_node->real_size;
        uint64_t b_size = b_node->real_size;

        Node* result_node = add_node(&result, hash);

        uint64_t a_index = 0, b_index = 0;

        while (a_index < a_size && b_index < b_size)
        {
            Segment a_segment = a_node->segments[a_index];
            Segment b_segment = b_node->segments[b_index];
            Segment new_segment;
            new_segment.begin = max(a_segment.begin, b_segment.begin);
            new_segment.end = min(a_segment.end, b_segment.end);
            
            if (new_segment.begin < new_segment.end)
            {
                add_segment(result_node, new_segment);
            }
            
            if (a_segment.end < b_segment.end) a_index++;
            else b_index++;
        }

        a_node++;
        b_node++;
    }

    return result;
}

zbior_ary roznica(zbior_ary A, zbior_ary B)
{
    zbior_ary result = constructor();

    Node* a_flattened_begin;
    uint64_t a_flattened_size;
    flatten(A.tree, &a_flattened_begin, &a_flattened_size);
    Node* a_flattened_end = a_flattened_begin + a_flattened_size;
    Node* a_node = a_flattened_begin;

    Node* b_flattened_begin;
    uint64_t b_flattened_size;
    flatten(B.tree, &b_flattened_begin, &b_flattened_size);
    Node* b_flattened_end = b_flattened_begin + b_flattened_size;
    Node* b_node = b_flattened_begin;

    while (a_node < a_flattened_end || b_node < b_flattened_end)
    {
        uint64_t a_hash = a_node < a_flattened_end ? a_node->hash : ULLONG_MAX;
        uint64_t b_hash = b_node < b_flattened_end ? b_node->hash : ULLONG_MAX;

        if (a_hash < b_hash)
        {
            paste_node(&result, a_node);
            a_node++;
            continue;
        }
        if (a_hash > b_hash)
        {
            b_node++;
            continue;
        }
        
        uint64_t hash = a_node->hash;

        uint64_t a_size = a_node->real_size;
        uint64_t b_size = b_node->real_size;

        Node* result_node = add_node(&result, hash);
        uint64_t b_index = 0;

        for (uint64_t a_index = 0; a_index < a_size; a_index++)
        {
            Segment a_segment = a_node->segments[a_index];

            while (b_index < b_size)
            {
                Segment b_segment = b_node->segments[b_index];

                if (b_segment.begin >= a_segment.end) break;

                if (b_segment.end <= a_segment.begin)
                {
                    b_index++;
                    continue;
                }

                if (b_segment.begin > a_segment.begin)
                {
                    Segment new_segment = {a_segment.begin, b_segment.begin};
                    add_segment(result_node, new_segment);
                }

                a_segment.begin = b_segment.end;

                if (b_segment.end > a_segment.end) break;

                b_index++;
            }

            if (a_segment.begin < a_segment.end)
            {
                add_segment(result_node, a_segment);
            }
        }

        a_node++;
        b_node++;
    }

    return result;
}

bool nalezy(zbior_ary A, int b)
{
    uint64_t hash = get_hash(b);
    Node* node = add_node(&A, hash);

    int begg = -1, endd = (int) node->real_size;

    while (endd - begg > 1)
    {
        int midd = (begg + endd) / 2;

        if (node->segments[midd].begin <= b) begg = midd;
        else endd = midd;
    }

    if (begg == -1 || node->segments[begg].end <= b) return false;
    else return true;
}

unsigned moc(zbior_ary A)
{
    uint64_t result = 0;

    Node* flattened_begin;
    uint64_t flattened_size;
    flatten(A.tree, &flattened_begin, &flattened_size);
    Node* flattened_end = flattened_begin + flattened_size;

    for (Node* a_node = flattened_begin; a_node < flattened_end; a_node++)
    {
        for (uint64_t i = 0; i < a_node->real_size; i++)
        {
            int element_count = (a_node->segments[i].end - a_node->segments[i].begin) / global_q;
            result += (uint64_t) element_count;
        }
    }

    return (unsigned) result;
}

unsigned ary(zbior_ary A)
{
    uint64_t result = 0;

    Node* flattened_begin;
    uint64_t flattened_size;
    flatten(A.tree, &flattened_begin, &flattened_size);
    Node* flattened_end = flattened_begin + flattened_size;

    for (Node* a_node = flattened_begin; a_node < flattened_end; a_node++)
    {
        result += a_node->real_size;
    }

    return (unsigned) result;
}

int comp(const void* a, const void* b)
{
    const int* int_a = (const int*) a;
    const int* int_b = (const int*) b;
    return *int_a - *int_b;
}

void print(zbior_ary A)
{
    Node* flattened_begin;
    uint64_t flattened_size;
    flatten(A.tree, &flattened_begin, &flattened_size);
    Node* flattened_end = flattened_begin + flattened_size;

    int elements[1000];
    uint64_t element_count = 0;

    for (Node* a_node = flattened_begin; a_node < flattened_end; a_node++)
    {
        for (uint64_t i = 0; i < a_node->real_size; i++)
        {
            for (int element = a_node->segments[i].begin; element < a_node->segments[i].end; element += global_q)
            {
                elements[element_count] = element;
                element_count++;
            }
        }
    }

    qsort(elements, element_count, sizeof(int), comp);

    printf("elements:\n");
    for (uint64_t i = 0; i < element_count; i++) printf("%d ", elements[i]);
    printf("\n");
}