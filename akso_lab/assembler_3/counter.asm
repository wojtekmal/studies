global counter

section .data
count: dd 0

section .text
counter:
    inc DWORD [rel count]
    mov rax, [rel count]
    ret