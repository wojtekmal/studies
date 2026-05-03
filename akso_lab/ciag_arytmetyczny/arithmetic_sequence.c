#include <inttypes.h>
#include <stddef.h>
#include <string.h>


typedef struct {
  uint64_t lo;
  int64_t  hi;
} int128_t;

int128_t arithmetic_sequence
    (uint64_t const *A0, uint64_t const *A1, uint64_t *Ak, size_t n, int64_t k)
{
    memcpy(Ak, A0, n);
    __uint128_t uk = k;
    
    for (int i = 0; i < n; i++)
    {
        uint64_t diff = A1[i] - A0[i];
        __uint128_t prod = uk * diff;

        if (*((__uint128_t*) ))
    }
}