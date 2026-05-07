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

    % rbx will hold remainder.
    xor rbx, rbx

    % r13 will hold the high bits from the previous iteration.
    xor r13, r13

    % r15 will hold the current position (goes from 0 to n - 1).
    xor r15, r15

main_loop:
    % Copy current block from A0 into r9.
    mov r9 [rdi + 8*r15]

    % Copy current block from A1 into r10 and rax.
    mov r10 [rsi + 8*r15]
    mov r10 rax

    % Store the difference in r11.
    sub rax r9

    % Multiply (u64) (A1[i] - A0[i]) and (u64) k.
    mul r8

    cmp 