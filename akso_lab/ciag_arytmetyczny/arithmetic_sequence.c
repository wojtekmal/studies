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
    
    for (int i = 0; i < n; i++)
    {
        
    }
}