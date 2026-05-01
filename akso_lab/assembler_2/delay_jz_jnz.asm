global delay

delay:
    rdtsc           ; liczba cykli zegara w parze rejestrów edx, eax
    shl   rdx, 0x20
    or    rax, rdx  ; liczba cykli zegara w rejestrze rax
    mov r12, rax

.loop
    sub rdi, 0x1
    jnz .loop

    rdtsc           ; liczba cykli zegara w parze rejestrów edx, eax
    shl   rdx, 0x20
    or    rax, rdx  ; liczba cykli zegara w rejestrze rax
    mov r12, rax