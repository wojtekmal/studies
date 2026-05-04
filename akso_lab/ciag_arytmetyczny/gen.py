import random
import numpy as np


def hex_repr(x, w):
    x = np.binary_repr(x, w)
    r = ""
    digits = "0123456789abcdef"
    for i in range(0, w, 4):
        d = int(x[i : i + 4], 2)
        r += digits[d]
    return r


def formatuj(A0, A1, n, k):
    # {(uint64_t[3]){4, 0, 0}, (uint64_t[3]){2, 0, 0}, (uint64_t[3]){10, 0, 0}, 3, -6, {0, 0}}
    Ak = A0 + (A1 - A0) * k
    A0_repr = hex_repr(A0, 64 * n)
    A1_repr = hex_repr(A1, 64 * n)
    Ak_repr = hex_repr(Ak, 64 * n + 128)

    # print(Ak)
    w = "{(uint64_t[" + str(n) + "]){"
    for i in range(n - 1, -1, -1):
        w += '0x' + A0_repr[i * 16 : i * 16 + 16] + "ULL,"
    w = w[:-1]
    w += "}, (uint64_t[" + str(n) + "]){"
    for i in range(n - 1, -1, -1):
        w += '0x' + A1_repr[i * 16 : i * 16 + 16] + "ULL,"

    w = w[:-1]
    w += "}, (uint64_t[" + str(n) + "]){"
    for i in range(n + 1, 1, -1):
        w += '0x' + Ak_repr[i * 16 : i * 16 + 16] + "ULL,"

    w = w[:-1]
    w += "}, " + str(n) + "," + str(k) + f", {{{'0x' + Ak_repr[16:32]}ULL,{'0x' + Ak_repr[0:16]}}}}}"
    w+=f",//{hex(A0)} {hex(A1)} {hex(Ak)} {hex(k)}"
    return w

for i in range(5):
    n = 4
    k = -(2**63) + 1
    # k = random.randint(-(2**63), 2**63 - 1)
    # k = random.randint(0, 2**63 - 1)
    # k = random.randint(0, 2**63 - 1)
    # k =

    # max_a = 2 ** (n * 64 - 1) - 1
    min_a = -(2 ** (n * 64 - 1))  + 1
    max_a = min_a + 3
    # min_a = 0

    A1 = random.randint(min_a, max_a)
    A0 = random.randint(min_a, max_a)
    # A1 = random.randint(min_a, max_a)
    # Ak = A0 + (A1 - A0) * k

    print(formatuj(A0, A1, n, k))

# print(
#     f"""A0 = {hex_repr(A0, n*64)}
# A1 = {hex_repr(A1, n*64)}
# Ak = {hex_repr(Ak, (n + 2)*64)}
# k = {hex_repr(k, 64)}
# n = {n}"""
# )
