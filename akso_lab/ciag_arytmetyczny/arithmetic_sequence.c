#include <inttypes.h>
#include <stddef.h>
#include <string.h>
#include <limits.h>
#include <stdio.h>

typedef struct {
  uint64_t lo;
  int64_t  hi;
} int128_t;

int128_t arithmetic_sequence
    (uint64_t const *A0, uint64_t const *A1, uint64_t *Ak, size_t n, int64_t k)
{
    uint64_t prev_high_bits = 0;
    uint64_t prev_middle_bits = 0;
    uint64_t ku = k;
    
    for (int i = 0; i < n; i++)
    {
        uint64_t diff = A1[i] - A0[i];

        __uint128_t prod = (__uint128_t) ku * diff;

        uint64_t middle_bits = prod >> 64;
        uint64_t low_bits = prod & (-1ULL);

        if (k < 0) middle_bits -= diff;
        if (A1[i] < A0[i]) middle_bits -= ku;

        uint64_t new_high_bits = -(middle_bits >= 0x8000000000000000);
        
        if (low_bits > ULLONG_MAX - prev_middle_bits) prev_high_bits++;
        low_bits += prev_middle_bits;

        if (low_bits > ULLONG_MAX - A0[i]) prev_high_bits++;
        low_bits += A0[i];

        if (low_bits > ULLONG_MAX - Ak[i]) prev_high_bits++;
        Ak[i] += low_bits;

        new_high_bits += (middle_bits > ULLONG_MAX - prev_high_bits);
        new_high_bits -= (prev_high_bits >= 0x8000000000000000);

        middle_bits += prev_high_bits;

        prev_high_bits = new_high_bits;

        prev_middle_bits = middle_bits;
    }

    if ((int64_t) A0[n - 1] < 0)
    {
        if (k < 0 && -k > prev_middle_bits) prev_high_bits--;
        if (k > 0 && prev_middle_bits > ULLONG_MAX - k) prev_high_bits++;
        prev_middle_bits += k;
    }

    if ((int64_t) A1[n - 1] < 0)
    {
        if (k > 0 && k > prev_middle_bits) prev_high_bits--;
        if (k < 0 && prev_middle_bits > ULLONG_MAX + k) prev_high_bits++;
        prev_middle_bits -= k;
    }

    if (A0[n - 1] & (1ULL << 63))
    {
        if (prev_middle_bits == 0) prev_high_bits--;
        prev_middle_bits--;
    }

    int128_t result;
    result.hi = prev_high_bits;
    result.lo = prev_middle_bits;
    return result;
}