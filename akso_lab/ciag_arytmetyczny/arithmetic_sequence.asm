global arithmetic_sequence

arithmetic_sequence:
    ; Save the values of the registers that we're supposed to not change.
    push r15
    push r14
    push r13
    push r12
    push rbx

    ; r8 will hold k. It already happens to be here.

    ; rcx will hold n. It already happens to be here.

    ; r12 will hold the pointer to Ak
    mov r12, rdx

    ; rsi will hold the pointer to A1. It already happens to be here.

    ; rdi will hold the pointer to A0. It already happens to be here.

    ; rbx will hold the remainder.
    xor ebx, ebx

    ; r13 will hold the high bits from the previous iteration.
    xor r13, r13

    ; r15 will hold the current position (goes from 0 to n - 1).
    xor r15, r15

main_loop:
    ; Copy current block from A0 into r9.
    mov r9, [rdi + 8*r15]

    ; Copy current block from A1 into r10.
    mov r10, [rsi + 8*r15]

    ; Extend the previous remainder into 128 bits and store the extension.
    mov rax, rbx
    cqo
    mov r14, rdx

    ; Add A0[i] to the previous high bits and accumulate the carry in the
    ; remainder and the extension of the remainder.
    add r13, r9
    adc rbx, 0
    adc r14, 0

    ; Store (u64) (A1[i] - A0[i]) in rdx.
    mov rdx, r10
    sub rdx, r9

    ; Multiply (u64) (A1[i] - A0[i]) and (u64) k.
    mulx rax, r11, r8

    ; If (u64) A1[i] < (u64) A0[i], subtract (u64) k from the high bits in
    ; in order to make up for the difference. Note that we already have the sign
    ; flag set by the calculation of (u64) (A1[i] - A0[i]).
    jae after_sub_k
    sub rax, r8
after_sub_k:

    ; If (i64) k < 0, subtract the difference from the high bits of the product.
    test r8, r8
    jge after_sub_diff
    sub rax, rdx
after_sub_diff:

    ; Right now rax:r11 holds a signed 128 bit representation of
    ; k * (A1[i] - A0[i]), where k is signed and A1[i], A0[i] are unsigned.

    ; Extend the higher bits of the product into 128 bits.
    cqo

    ; We have 3 pairs of numbers on the low, medium and high bits respectively.

    ; Sum the previously computed A0[i] + previous high bits and the current
    ; low bits.
    add r11, r13

    ; Sum the high bits of the product and the previous remainder while taking
    ; into account the carry flag from the previous addition:
    adc rbx, rax

    ; Sum the new remainder and the extension of the old remainder while taking
    ; into account the carry flag from the previous addition:
    adc r14, rdx

    ; Right now we have what would be the answer if A0 and A1 would
    ; be cut of to the lowest (i + 1) * 64 bits and treated as unsigned numbers.
    ; The answer is in the following form: i blocks from the previous loop
    ; iterations are already stored in Ak and the highest 192 bits of the
    ; (signed) answer are stored in the above registers.

    ; Save the lowest bits of the sum in Ak[i].
    mov [r12 + 8*r15], r11

    ; Save the medium bits of the sum as the high bits of the (soon to be)
    ; previous iteration.
    mov r13, rbx

    ; Save the high bits of the sum as the remainder of the (soon to be)
    ; previous iteration.
    mov rbx, r14

    ; Check if the loop should end by incrementing i and checking if i == n.
    inc r15
    cmp r15, rcx
    jne main_loop

    ; After the loop we'd have the answer were A0 and A1 unsigned. They're not,
    ; so we have to take this into account.

    ; First we convert k into 128 bits, so that we can add and subtract it from
    ; the high bits and remainder from the previous iteration.
    mov rax, r8
    cqo

    ; If A0 is actually negative, we add k * 2^(n*64) from the answer, which is
    ; equivalent to adding k to the highest 128 bits of the answer. Also we have
    ; to decrement the highest 128 bits of the answer, because A0 has two roles
    ; - the answer is A0 + (A1 - A0) * k.
    ; Conveniently, the saved value of A0[n - 1] is not overwritten in the last
    ; iteration of the loop.

    ; Check if A0 is negative.
    test r9, r9
    jge after_A0_negative

    ; Add k.
    add r13, r8
    adc rbx, rdx

    ; Decrement the highest 128 bits.
    sub r13, 1
    sbb rbx, 0
after_A0_negative:

    ; If A1 is actually negative, we subtract k from the highest 128 bits.

    ; Check if A1 is negative.
    test r10, r10
    jge after_A1_negative

    ; Subtract k.
    sub r13, r8
    sbb rbx, rdx
after_A1_negative:

    ; Transfer the highest 128 bits into rax and rdx.
    mov rax, r13
    mov rdx, rbx

    ; Restore the registers and return.
    pop rbx
    pop r12
    pop r13
    pop r14
    pop r15
    ret
