global arithmetic_sequence

arithmetic_sequence:
    % Save the values of the registers that we're supposed to not change.
    push r15
    push r14
    push r13
    push r12
    push rbx

    % rbx will hold k
    mov rbx, r8

    % r14 will hold n
    mov r14, rcx

    % r12 will hold pointer to Ak
    mov r12, rdx

    % r15 will hold pointer to A1
    mov r15, rsi

    % r13 will hold pointer to A0
    mov r13, rdi

    