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

    % Extend the previous remainder into 128 bits.
    mov rax, rbx
    cqo

    % rax is free, because rbx also holds the low bits of the previous remainder.

    % At this moment we have 7 summands: on the lowest bits we have the low bits
    % of the product, A0[i] and the previous high bits, which are in r10, r9,
    % r13 respectively, on the medium bits we have the high bits of the product
    % and the previous remainder, in r11 and rbx respectively, and the new
    % remainder and the extension of the old remainder in the highest bits,
    % in rcx and rdx respectively. The highest bits are signed, while the medium
    % and lower bits are unsigned, as if they all represented parts of signed
    % 192 bit numbers.
    % We start with summing three pairs of numbers: everything mentioned above,
    % excluding the previous high bits (this choice is arbitrary).

    % Sum the lower bits of the product and A0[i].
    add r10, r9

    % Sum the high bits of the product and the previous remainder while taking
    % into account the carry flag from the previous addition:
    adc r11, rbx

    % Sum the new remainder and the extension of the old remainder while taking
    % into account the carry flag from the previous addition:
    adc rdx, rcx

    % Now we have 3 sums, on the low, medium and high positions respectively,
    % which collectively represent a signed 192 bit number. We also have to sum
    % the previous high bits with this number. We do this by summing with the
    % lower bits and carrying over to the medium and high bits.
    add r10, r13
    adc r11, 0
    adc rdx, 0

    % Right now we have what would be the answer if A0 and A1 would
    % be cut of to the lowest (i + 1) * 64 bits and treated as unsigned numbers.
    % The answer is in the following form: i blocks from the previous loop
    % iterations are already stored in Ak and the highest 192 bits of the
    % (signed) answer are stored in the above registers.

    % Save the lowest bits of the sum in Ak[i].
    mov [r12 + 8*r15] r10

    % Save the medium bits of the sum as the high bits of the (soon to be)
    % previous iteration.
    mov r13, r11

    % Save the high bits of the sum as the remainder of the (soon to be)
    % previous iteration.
    mov rbx, rdx

    % Check if the loop should end by incrementing i and checking if i == n.
    inc r15
    cmp r15 r14
    jne main_loop

    % After the loop we'd have the answer were A0 and A1 unsigned. They're not,
    % so we have to take this into account.

    % First we convert k into 128 bits, so that we can add and subtract it from
    % the high bits and remainder from the previous iteration.
    mov rax, r8
    cqo

    % If A0 is actually negative, we add k * 2^(n*64) from the answer, which is
    % equivalent to adding k to the highest 128 bits of the answer. Also we have
    % to decrement the highest 128 bits of the answer, because A0 has two roles
    % - the answer is A0 + (A1 - A0) * k.
    % Conveniently, the saved value of A0[n - 1] is not overwritten in the last
    % iteration of the loop.

    % Check if A0 is negative.
    cmp r9, 0
    jge after_A0_negative

    % Add k.
    add r13, r8
    adc rbx, rdx

    % Decrement the highest 128 bits.
    sub r13 1
    sbb
after_A0_negative:

    % If A1 is actually negative, we subtract k from the highest 128 bits.
    % Unfortunately we have to access A1[n - 1] again.

    % Check if A1 is negative.
    cmp [rsi + 8*r14 - 8] 0
    jge after_A1_negative

    % Subtract k.
    sub r13, r8
    sbb rbx, rdx
after_A1_negative:

    %
