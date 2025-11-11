#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>
#include <string.h>

// Unsigned integers are used everywhere in the code. This makes things
// simpler, mainly by avoiding comparisons of different types and avoiding
// casts in function calls. -1U (short for UINT_MAX) is used throughout the
// code as a "null" value, signifying nonexistant motels.
typedef unsigned uint;

// Holds three integers. Not a specific kind of three integers, but it happens
// to be that an array of three integers is a common pattern in my code.
typedef uint Triplet[3];
#define min(a, b) (a < b ? a : b)
#define max(a, b) (a > b ? a : b)

// Made these global instead of passing them into the functions. A context
// struct could have been used.
uint* types;
uint* positions;

void insert_closer_to_triplet(Triplet triplet, uint new_index, bool is_left)
{
    // Here triplet is a Triplet that holds the three closest motels of
    // different type, in one direction, to the motel at position new_index.
    // If there are less than three types of motels in that direction, then
    // triplet is filled with -1U's. This function updates triplet to also
    // hold new_index, so that triplet now contains the three closest motels
    // to the next motel (to the left or right).
    uint new_type = types[new_index];

    for (uint i = 0; i < 3; i++)
    {
        // If a motel of the same type is already in triplet, then the new
        // closest motel of that type is the one at new_index.
        if (triplet[i] != -1U && types[triplet[i]] == new_type)
        {
            triplet[i] = new_index;
            return;
        }
    }

    for (uint i = 0; i < 3; i++)
    {
        // If triplet has an empty slot, then we can just fill it.
        if (triplet[i] == -1U)
        {
            triplet[i] = new_index;
            return;
        }
    }

    // If all the motels in triplet are of a different type than new_index,
    // then we find the furthest motel and remember it's position in triplet,
    // in order to substitute it with new_index.
    uint index_of_furthest_in_triplet = -1U;
    uint furthest = is_left ? -1U : 0;

    for (uint i = 0; i < 3; i++)
    {
        if (is_left && triplet[i] <= furthest)
        {
            furthest = triplet[i];
            index_of_furthest_in_triplet = i;
        }
        if (!is_left && triplet[i] >= furthest)
        {
            furthest = triplet[i];
            index_of_furthest_in_triplet = i;
        }
    }

    triplet[index_of_furthest_in_triplet] = new_index;
}

void insert_further_to_triplet(Triplet triplet, uint new_index)
{
    // Updates a triplet that holds the three furthest motels in of different
    // types in a particular direction. This is simpler than holding the three
    // closest motels, because the motels in triplet don't have to be replaced.
    // type_count holds the number of non empty slots in triplet (which is also
    // the number of distinct types of motels that have been found so far).
    uint type_count = 0;

    while (type_count < 3 && triplet[type_count] != -1U) type_count++;

    // If triple already holds three motels of different types, then there is
    // nothing to update.
    if (type_count == 3) return;

    // Make sure that triplet doesn't already hold a motel of the same type.
    for (uint i = 0; i < type_count; i++)
    {
        if (types[new_index] == types[triplet[i]]) return;
    }

    triplet[type_count] = new_index;
}

bool is_good_triplet(Triplet triplet)
{
    // Here triplet is a Triplet of motels that we want to check if they are
    // the closest or most distant Triplet. We have to make sure that they are
    // not null and that they are ordered correctly and of different types.
    for (uint i = 0; i < 3; i++)
    {
        if (triplet[i] == -1U) return false;
    }

    for (uint i = 0; i < 2; i++)
    {
        if (triplet[i] > triplet[i + 1]) return false;
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
    // triple where the given motel is in the middle. The method that
    // obviously doesn't work is finding the closest motel from the left
    // and the closest one from the right which are of a different type.
    // Unfortunately, the one on the left and the one on the right may be
    // of the same type. To fix that, one may find the two closest motels
    // that have pairwise different types and types different than that of the
    // middle motel, from both directions. It turns out to be simpler to
    // remember three motels from both sides and not worry about them having
    // different types than the middle one, which is what we do here.
    uint n;
    scanf("%u", &n);

    types = malloc(n * sizeof(uint));
    positions = malloc(n * sizeof(uint));

    for (uint i = 0; i < n; i++)
    {
        scanf("%u %u", &types[i], &positions[i]);
    }

    // For each motel, left_closest and right_closest hold the three closest
    // motels on a given side with pairwise different types. They are filled
    // by iterating over the motels, keeping an updated Triplet and filling
    // the data for the given motel.
    Triplet* left_closest = malloc(n * sizeof(Triplet));
    Triplet* right_closest = malloc(n * sizeof(Triplet));

    Triplet current_left_closest = {-1U, -1U, -1U};
    Triplet current_right_closest = {-1U, -1U, -1U};

    for (uint i = 0; i < n; i++)
    {
        memcpy(left_closest[i], current_left_closest, sizeof(Triplet));
        insert_closer_to_triplet(current_left_closest, i, true);
    }

    // i >= 0 wouldn't work because i is unsigned.
    for (uint i = n - 1; i != -1U; i--)
    {
        memcpy(right_closest[i], current_right_closest, sizeof(Triplet));
        insert_closer_to_triplet(current_right_closest, i, false);
    }

    // Note that -1U is a large number.
    uint global_min = -1U;

    for (uint i = 0; i < n; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 3; k++)
            {
                // For each motel in the left Triplet and each in the right,
                // we check if these, along with motel i, are the closest.
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

    // The furthest motels to the left and to the right are the same for all
    // motels. Similarly to finding the closest Triplet, we iterate over all
    // motels and try to insert them into the Triplet.
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

    // 0 is the lowest possible answer. It's also the answer that should be
    // given if no Triplet is found, so we can just set global_max to 0 at the
    // start and not worry about the no good triplet case.
    uint global_max = 0;

    for (uint i = 0; i < n; i++)
    {
        for (int j = 0; j < 3; j++)
        {
            for (int k = 0; k < 3; k++)
            {
                // Similarly as in finding close motels, we check all
                // combinations and pick the best.
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

    // In the case that no good triplet is found, the answer is 0 0.
    if (global_min == -1U) global_min = 0;
    printf("%u %u\n", global_min, global_max);

    free(positions);
    free(types);
    free(left_closest);
    free(right_closest);
}