global smax, max

smax:
    mov rax rdi
    cmp rdi rsi
    cmov rax rsi