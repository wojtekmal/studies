#include <inttypes.h>
#include <stddef.h>
#include <string.h>
#include <limits.h>
#include <stdio.h>

typedef struct {
  uint64_t lo;
  int64_t  hi;
} int128_t;

__uint128_t multiply_with_128(uint64_t a, uint64_t b)
{
    return (__uint128_t) a * b;
}

int128_t arithmetic_sequence
    (uint64_t const *A0, uint64_t const *A1, uint64_t *Ak, size_t n, int64_t k)
{
    memcpy(Ak, A0, n * sizeof(uint64_t));

    int64_t remainder = 0, prev_high_bits = 0;
    
    for (int i = n - 1; i >= 0; i--)
    {
        uint64_t diff;
        uint64_t ku;

        if (A1[i] >= A0[i])
        {
            diff = A1[i] - A0[i];
            ku = k;
        }
        else
        {
            diff = A0[i] - A1[i];
            ku = -k;
        }

        __uint128_t prod = multiply_with_128(ku, diff);

        uint64_t high_bits = prod >> 64;
        uint64_t low_bits = prod & (-1ULL);

        if (ku & (1ULL << 63)) high_bits -= diff;

        if (low_bits > ULLONG_MAX - prev_high_bits) remainder++;
        low_bits += prev_high_bits;

        if (low_bits > ULLONG_MAX - Ak[i]) remainder++;
        Ak[i] += low_bits;

        high_bits += remainder;
        
        if (high_bits & (1ULL << 63)) remainder = -1ULL;
        else remainder = 0;

        prev_high_bits = high_bits;
    }

    int128_t result;
    result.hi = remainder;
    result.lo = prev_high_bits;
    return result;
}

int main()
{
    uint64_t A0 = 0, A1 = 1, Ak;

    arithmetic_sequence(&A0, &A1, &Ak, 1, -5);

    printf("%lu\n", Ak);
}