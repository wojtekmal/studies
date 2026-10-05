global smax, umax

smax:
    mov eax, esi
    cmp edi, esi
    cmovg eax, edi
    ret

umax:
    mov eax, esi
    cmp edi, esi
    cmova eax, edi
    ret