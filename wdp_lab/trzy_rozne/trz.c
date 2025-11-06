#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>
#include <string.h>
typedef unsigned uint;
typedef uint Triplet[3];
#define min(a, b) (a < b ? a : b)
#define max(a, b) (a > b ? a : b)

uint* types;
uint* positions;

void insert_closer_to_triplet(Triplet triplet, uint new_index, bool is_left)
{
    uint new_type = types[new_index];

    for (uint i = 0; i < 3; i++)
    {
        if (triplet[i] != -1U && types[triplet[i]] == new_type)
        {
            triplet[i] = new_index;
            return;
        }
    }

    for (uint i = 0; i < 3; i++)
    {
        if (triplet[i] == -1U)
        {
            triplet[i] = new_index;
            return;
        }
    }

    uint oldest_triplet_index = -1U;
    uint oldest_position = is_left ? -1U : 0;

    for (uint i = 0; i < 3; i++)
    {
        if (is_left && positions[triplet[i]] <= oldest_position)
        {
            oldest_position = positions[triplet[i]];
            oldest_triplet_index = i;
        }
        if (!is_left && positions[triplet[i]] >= oldest_position)
        {
            oldest_position = positions[triplet[i]];
            oldest_triplet_index = i;
        }
    }

    triplet[oldest_triplet_index] = new_index;
}

void insert_further_to_triplet(Triplet triplet, uint new_index)
{
    uint type_count = 0;

    while (type_count < 3 && triplet[type_count] != -1U) type_count++;

    if (type_count == 3) return;

    for (uint i = 0; i < type_count; i++)
    {
        if (types[new_index] == types[triplet[i]]) return;
    }

    triplet[type_count] = new_index;
}

bool is_good_triplet(Triplet triplet)
{
    for (uint i = 0; i < 3; i++)
    {
        if (triplet[i] == -1U) return false;
    }

    for (uint i = 0; i < 2; i++)
    {
        if (positions[triplet[i]] > positions[triplet[i + 1]]) return false;
    }

    for (uint i = 0; i < 3; i++)
    {
        for (uint j = i + 1; j < 3; j++)
        {
            if (types[triplet[i]] == types[triplet[j]]) return false;
        }
    }

    return true;
}

int main()
{
    // For each motel, the program calculates the closest and most distant
    // triple where the given motel is in the middle.
    uint n;
    scanf("%u", &n);

    positions = malloc(n * sizeof(uint));
    types = malloc(n * sizeof(uint));

    for (uint i = 0; i < n; i++)
    {
        scanf("%u %u", &types[i], &positions[i]);
    }

    Triplet* left_closest = malloc(n * sizeof(Triplet));
    Triplet* right_closest = malloc(n * sizeof(Triplet));

    Triplet current_left_closest = {-1U, -1U, -1U};
    Triplet current_right_closest = {-1U, -1U, -1U};

    for (uint i = 0; i < n; i++)
    {
        memcpy(left_closest[i], current_left_closest, sizeof(Triplet));
        insert_closer_to_triplet(current_left_closest, i, true);
    }

    for (uint i = n - 1; i != -1U; i--)
    {
        memcpy(right_closest[i], current_right_closest, sizeof(Triplet));
        insert_closer_to_triplet(current_right_closest, i, false);
    }

    uint global_min = -1U;

    for (uint i = 0; i < n; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 3; k++)
            {
                Triplet possible_best =
                    {left_closest[i][j], i, right_closest[i][k]};

                if (is_good_triplet(possible_best))
                {
                    uint left_pos = positions[possible_best[0]];
                    uint middle_pos = positions[possible_best[1]];
                    uint right_pos = positions[possible_best[2]];

                    uint first_dist = middle_pos - left_pos;
                    uint second_dist = right_pos - middle_pos;
                    global_min = min(global_min, max(first_dist, second_dist));
                }
            }
        }
    }

    Triplet left_furthest = {-1U, -1U, -1U};
    Triplet right_furthest = {-1U, -1U, -1U};

    for (uint i = 0; i < n; i++)
    {
        insert_further_to_triplet(left_furthest, i);
    }

    for (uint i = n - 1; i != -1U; i--)
    {
        insert_further_to_triplet(right_furthest, i);
    }

    uint global_max = 0;

    for (uint i = 0; i < n; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 3; k++)
            {
                Triplet possible_best =
                    {left_furthest[j], i, right_furthest[k]};

                if (is_good_triplet(possible_best))
                {
                    uint left_pos = positions[possible_best[0]];
                    uint middle_pos = positions[possible_best[1]];
                    uint right_pos = positions[possible_best[2]];

                    uint first_dist = middle_pos - left_pos;
                    uint second_dist = right_pos - middle_pos;
                    global_max = max(global_max, min(first_dist, second_dist));
                }
            }
        }
    }

    if (global_min == -1U) global_min = 0;
    printf("%u %u\n", global_min, global_max);

    free(positions);
    free(types);
    free(left_closest);
    free(right_closest);
}