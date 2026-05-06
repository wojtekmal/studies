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
    memcpy(Ak, A0, n * sizeof(uint64_t));

    int64_t remainder = 0;
    uint64_t prev_high_bits = 0;
    uint64_t ku = k;
    
    for (int i = 0; i < n; i++)
    {
        uint64_t diff = A1[i] - A0[i];

        __uint128_t prod = (__uint128_t) ku * diff;

        uint64_t high_bits = prod >> 64;
        uint64_t low_bits = prod & (-1ULL);

        if (k < 0) high_bits -= diff;

        if (A1[i] < A0[i]) high_bits -= ku;

        if (low_bits > ULLONG_MAX - prev_high_bits) remainder++;
        low_bits += prev_high_bits;

        if (low_bits > ULLONG_MAX - Ak[i]) remainder++;
        Ak[i] += low_bits;

        high_bits += remainder;
        
        if (high_bits & (1ULL << 63)) remainder = -1ULL;
        else remainder = 0;

        prev_high_bits = high_bits;
    }

    if ((int64_t) A0[n - 1] < 0) prev_high_bits += ku;
    if ((int64_t) A1[n - 1] < 0) prev_high_bits -= ku;
    
    if (prev_high_bits & (1ULL << 63)) remainder = -1ULL;
    else remainder = 0;

    if (A0[n - 1] & (1ULL << 63))
    {
        if (prev_high_bits == 0) remainder--;
        prev_high_bits--;
    }

    int128_t result;
    result.hi = remainder;
    result.lo = prev_high_bits;
    return result;
}

//int main()
//{
//    uint64_t A0 = 0, A1 = 1, Ak;
//
//    int128_t high_bits = arithmetic_sequence(&A0, &A1, &Ak, 1, -5);
//
//    printf("%lx\n", (uint64_t) high_bits.hi);
//    printf("%lx\n", high_bits.lo);
//    printf("%lx\n", Ak);
//}