global arithmetic_sequence

arithmetic_sequence:
    ; First a description of the algorithm.
    ; 1. We write Ak = (A1 - A0) * k + A0.
    ;
    ; 2. We divide (A1 - A0) and A0 into blocks of 64 bits which can be operated
    ; on by standard instructions. There are n blocks, which we number from 0
    ; to n - 1 inclusively.
    ;
    ; 3. For the moment we do the calculations as if A0 and A1 were unsigned
    ; integers. That way A0 is the sum of 2^(64*i)*A0i, where i is the number of
    ; the block and A0i is the unsigned integer represented by block i. Later
    ; we will deal with the signedness of the last blocks. Note that we are only
    ; ignoring the signedness of the last blocks of A0 and A1; we still treat
    ; k and (A0 - A1) and Ak as signed integers.
    ;
    ; 4. We can represent Ak as the sum of 2^(64*i)*((A1i - A0i) * k + A0i).
    ; Unfortunately this isn't as simple as calculating each block of Ak
    ; separately, because the first n blocks of Ak are unsigned 64 bit integers,
    ; as in Ak can be represented as the sum of 2^(64*i)*Aki, were Aki are
    ; all unsigned except for Ak(n+1), and (A1i - A0i) * k + A0i doesn't happen
    ; to fit in a 64 bit unsigned integer. It fits in a signed 192 bit integer,
    ; so an integer represented by 3 64 bit blocks. We will call these blocks
    ; the low, middle and high blocks, respectively. Note that "low block" may
    ; refer to the the low blocks of different numbers, such as (A1i - A0i) * k
    ; or (A1i - A0i) * k + A0i, so clarification will be given where context
    ; isn't sufficient.
    ;
    ; 5. Each step i will pass the middle and high bits on to the next step and
    ; save its low bits as Aki. This means that after calculating
    ; (A1i - A0i) * k + A0i, we have to accumulate the middle and high blocks 
    ; from the previous step. Since they are from the previous step, they 
    ; account for 2^(64*(i-1)) in the sum for Ak, so they correspond to the
    ; current low and middle blocks, respectively. The previous high block
    ; represents a signed integer, so the previous blocks which we want to
    ; accumulate collectively represent a 128 bit signed integer. In order to
    ; add it to our 192 bit signed integer, we have to extend it to 192 bits.
    ; By the nature of U2, we can do this by simply extending the previous high
    ; bits. After that we sum these two numbers by summing the corresponding
    ; blocks and taking carries into account, save the low block as Aki and pass
    ; on the higher two blocks.
    ;
    ; 6. In order to calculate (A1i - A0i) * k + A0i, we first calculate
    ; (A1i - A0i) * k and then add A0i. In order to calculate the product,
    ; we first calculate A1i - A0i by subtracting the unsigned 64 bit
    ; representations of A1i and A0i modulo 2^64 (which is done simply with the
    ; sub instruction). If A1i - A0i < 0, then this is actually
    ; A1i - A0i + 2^64. Similarly, for a moment we treat k as a unsigned number
    ; modulo 2^64, which is actually k + 2^64 if k < 0. Now we can multiply
    ; these two unsigned numbers with the mul instruction, which gives us an
    ; unsigned 128 bit number.
    ;
    ; 7. This is (A1i - A0i) * k if both factors are positive,
    ; (A1i - A0i + 2^64) * k if A1i - A0i < 0, (A1i - A0i) * (k + 2^64) if k < 0
    ; and (A1i - A0i + 2^64) * (k + 2^64) if both are negative. In order to get
    ; the unsigned 128 bit representation, we have to subtract 2^64 * k,
    ; 2^64 * (A1i - A0i) or both along with 2^64 * 2^64 = 2^128, depending on
    ; the case. Note that subtracting 2^128 is unnecessary, because we are
    ; working with 128 bit representations. By the nature of U2, this is also
    ; the signed representation of (A1i - A0i) * k modulo 2^128. (A1i - A0i) * k
    ; is an integer with an absolute value no greater than 2^127, so we actually
    ; obtained the signed 128 bit representation of (A1i - A0i) * k.
    ;
    ; 8. We extend into a 192 signed representation, add A0i while making sure
    ; to carry and proceed as described in the previous steps.
    ;
    ; 9. Passing on the unsigned middle block and the signed high block is
    ; convenient, because the last step (i = n - 1) will pass on what
    ; arithmetic_sequence is supposed to return: the signed representation of
    ; the highest 128 bits.
    ;
    ; 10. We still have to deal with the fact that we treated A0 and A1 as
    ; unsigned numbers in order for the calculations to be simpler. Remember
    ; that Ak = (A1 - A0) * k + A0. If A1 is actually negative, then what we
    ; calculated is actually (A1 - A0 + 2^(64*n)) * k + A0, which means that we
    ; have to subtract 2^(64*n) * k. This is equivalent to subtracting k from
    ; the middle block that the last step passed on and doing the carry in the
    ; high block that it passed on. If A0 is actually negative, we have to
    ; similarly add 2^(64*n) * k and also subtract 2^(64*n), because of the A0
    ; term at the end of the expression. These two steps can be done
    ; sequentially, i.e. instead of doing 4 cases were A0 and A1 are positive or
    ; negative, we can first make up for A0, then for A1.
    ;
    ; 11. Return the middle and high blocks from the last step of the loop.

    ; Save the callee-saved registers which we'll use.
    push rbp
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

    ; rbp will hold the current position (goes from 0 to n - 1). This register
    ; is used instead of one of the general purpose registers in order to save
    ; a byte in the code below.
    xor ebp, ebp

main_loop:
    ; Copy current block from A0 into r9.
    mov r9, [rdi + 8*rbp]

    ; Copy current block from A1 into r10.
    mov r10, [rsi + 8*rbp]

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
    mov [r12 + 8*rbp], r11

    ; Save the medium bits of the sum as the high bits of the (soon to be)
    ; previous iteration.
    mov r13, rbx

    ; Save the high bits of the sum as the remainder of the (soon to be)
    ; previous iteration.
    mov rbx, r14

    ; Check if the loop should end by incrementing i and checking if i == n.
    inc rbp
    cmp rbp, rcx
    jne main_loop
after_main_loop:

    ; After the loop we'd have the answer were A0 and A1 unsigned. They're not,
    ; so we have to take this into account.

    ; First we convert k into 128 bits, so that we can add and subtract it from
    ; the high bits and remainder from the previous iteration.
    mov rax, r8
    cqo

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
    pop rbp
    ret
