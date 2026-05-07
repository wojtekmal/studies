global hello
extern putchar

section .rodata
hello_text: db `Hello World!\n\0`
hello_len equ $ - hello_text

section .text

hello:
    push rbx
    xor rbx, rbx

hello_loop:
    lea rax, [rel hello_text]
    add rax, rbx
    movzx rdi, byte [rax]
    call putchar wrt ..plt
    inc rbx
    cmp rbx, hello_len
    jne hello_loop

    pop rbx
    ret