#include <stdint.h>

/**
 * Testy zawierające kombinacje maksymalnych i minialnych wartości dla n = 2
 */

static const test_data_t skrajne_przypadki[] = {
{(uint64_t[2]){0x0000000000000000ULL,0x8000000000000000ULL}, (uint64_t[2LL]){0xffffffffffffffffULL,0x7fffffffffffffffULL}, (uint64_t[2]){0x8000000000000000ULL,0x8000000000000000ULL}, 2ULL,-9223372036854775807LL - 1, {0x7fffffffffffffffULL,0xffffffffffffffff}},//-0x80000000000000000000000000000000 0x7fffffffffffffffffffffffffffffff -0x80000000000000007fffffffffffffff8000000000000000 -0x8000000000000000
{(uint64_t[2]){0x0000000000000000ULL,0x8000000000000000ULL}, (uint64_t[2LL]){0xffffffffffffffffULL,0x7fffffffffffffffULL}, (uint64_t[2]){0x8000000000000001ULL,0x7fffffffffffffffULL}, 2ULL,9223372036854775807LL, {0x7ffffffffffffffeULL,0x0000000000000000}},//-0x80000000000000000000000000000000 0x7fffffffffffffffffffffffffffffff 0x7ffffffffffffffe7fffffffffffffff8000000000000001 0x7fffffffffffffff
{(uint64_t[2]){0xffffffffffffffffULL,0x7fffffffffffffffULL}, (uint64_t[2LL]){0x0000000000000000ULL,0x8000000000000000ULL}, (uint64_t[2]){0x7ffffffffffffffeULL,0x8000000000000000ULL}, 2ULL,9223372036854775807LL, {0x8000000000000001ULL,0xffffffffffffffff}},//0x7fffffffffffffffffffffffffffffff -0x80000000000000000000000000000000 -0x7ffffffffffffffe7fffffffffffffff8000000000000002 0x7fffffffffffffff
{(uint64_t[2]){0xffffffffffffffffULL,0x7fffffffffffffffULL}, (uint64_t[2LL]){0x0000000000000000ULL,0x8000000000000000ULL}, (uint64_t[2]){0x7fffffffffffffffULL,0x7fffffffffffffffULL}, 2ULL,-9223372036854775807LL - 1, {0x8000000000000000ULL,0x0000000000000000}},//0x7fffffffffffffffffffffffffffffff -0x80000000000000000000000000000000 0x80000000000000007fffffffffffffff7fffffffffffffff -0x8000000000000000
};