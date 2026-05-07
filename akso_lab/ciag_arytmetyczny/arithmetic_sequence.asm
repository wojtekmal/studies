global arithmetic_sequence

arithmetic_sequence:
    % Save the values of the registers that we're supposed to not change.
    push r15
    push r14
    push r13
    push r12
    push rbx

    % r8 will hold k. It already happens to be here.

    % r14 will hold n
    mov r14, rcx

    % r12 will hold pointer to Ak
    mov r12, rdx

    % rsi will hold pointer to A1. It already happens to be here.

    % rdi will hold pointer to A0. It already happens to be here.

    % rbx will hold the remainder.
    xor rbx, rbx

    % r13 will hold the high bits from the previous iteration.
    xor r13, r13

    % r15 will hold the current position (goes from 0 to n - 1).
    xor r15, r15

main_loop:
    % Copy current block from A0 into r9.
    mov r9, [rdi + 8*r15]

    % Copy current block from A1 into r10 and rax.
    mov r10, [rsi + 8*r15]
    mov r11, r10

    % Store the difference in r11 and copy to rax.
    sub r11, r9
    mov rax, r11

    % Multiply (u64) (A1[i] - A0[i]) and (u64) k.
    mul r8

    % If (i64) A1[i] < (i64) A0[i], subrtract (u64) k from the high bits.
    cmp r10, r9
    jge after_sub_k
    sub rdx, r8
after_sub_k:

    % We won't need A1[i] anymore, so r10 is free.

    % If (i64) k < 0, subtract the difference from the high bits of the product.
    cmp r8, 0
    jge after_sub_diff
    sub rdx, r11
after_sub_diff:

    % We won't need the difference anymore, so r11 is free.
    % Right now rdx:rax holds a signed 128 bit representation of
    % k * (A1[i] - A0[i]), where k is signed and A1[i], A0[i] are unsigned.

    % Copy the lower bits of the product into r10.
    mov r10, rax

    % Extend the higher bits of the product into 128 bits.
    mov rax, rdx
    cqo
    
    % Copy the high bits of the product into r11 and the extended bits
    % (the new remainder) into rcx.
    mov r11, rax
    mov rcx, rdx

    % Extend the previous remainder into 128 bits. At this moment all available
    % registers are used.
    mov rax, rbx
    cqo

    % rax is free, because rbx also holds the low bits of the previous remainder.

    % At this moment we have 7 summands: on the lowest bits we have the low bits
    % of the product, A0[i] and the previous high bits, which are in r10, r9,
    % r13 respectively, on the medium bits we have the high bits of the product
    % and the previous remainder, in r11 and rbx respectively, and the new
    % remainder and the extension of the old remainder in the highest bits,
    % in rcx and rdx respectively.
