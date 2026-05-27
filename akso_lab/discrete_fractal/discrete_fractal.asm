SYS_READ equ 0
SYS_WRITE equ 1
SYS_BRK equ 12
SYS_EXIT equ 60

FD_READ equ 0
FD_WRITE equ 1

BUFFER_LEN equ 4096

INITIAL_STRING_NOT_READ equ 1
INITIAL_STRING_READ equ 0

NULLPTR equ 0

MOVE_BRK_BLOCK_LEN equ 4096

section .data

space_and_null: db ' ', '\0'

section .bss

; buffer is used for buffering input and output.
buffer: resb 4096

; expansions remembers what each character expands into. Note that expansions is
; zero initialized, which is used later in the program.
expansions: resb 128

section .text

    ; Do a syscall with rax = SYS_BRK and rdi = NULLPTR = 0. sys_brk, regardless
    ; of whether it succeeds, returns the current break pointer. This means that
    ; we can tell it to set brk to nullptr and it will silently fail and return
    ; the current brk in rax.
    mov eax, SYS_BRK
    xor edi, edi
    syscall

    ; r8 will hold the brk throughout the program. Now we save the initial brk.
    mov r8, rax

    ; Check that there are exactly 2 arguments: the program name and n. If not,
    ; then jump to the end of the program, where memory is freed and the program
    ; exits with an error.
    cmp [rsp], 2
    jne free_and_exit_error

    ; Store the pointer to the first byte of the second argument in rbx.
    mov rbx, [rsp + 16]

    ; Now rbx points to the first digit of n. We will accumulate the unsigned
    ; 32-bit value of n in eax. For each digit, we:
    ; 1. Breaking the loop if we found a null byte.
    ; 2. Check if it's a digit.
    ; 3. Accumulate in eax and check if there was an overflow.
    ; 4. Increment rbx and repeat.

    ; Start with zeroing eax and ecx, because they will be used to store n and
    ; the current digit, respectively.
    xor eax, eax
    xor ecx, ecx

    ; mul needs the second factor to be in a register. We will be multiplying by
    ; 10, so we store 10 in ebp (we won't need the initial value of rbp).
    mov ebp, 10

read_n_loop:
    ; Store the digit (one byte) in cl.
    mov cl, [rbx]

    ; Check if it's a null byte. If yes, then break the loop.
    test cl, cl
    je after_read_n_loop

    ; Check if cl holds a valid digit. We do this by comparing it to '0' and
    ; '9'. Here cl is treated as a 8-bit unsigned number.
    cmp cl, '0'
    jb free_and_exit_error
    cmp cl, '9'
    ja free_and_exit_error

    ; For convenience we subtract '0' from cl, so that cl stores the value
    ; represented by the digit, not the ascii encoding of the digit.
    sub cl, '0'

    ; TODO: use jcxz and first subtract '0', then check if it's a digit.

    ; We accumulate in eax in two steps: first we multiply eax by 10, then we
    ; add the digit stored in cl. Both steps require checking for overflows.

    ; Multiply n by 10 and check for overflows. Note that eax holds an unsigned
    ; 32-bit representation of n, so we use mul. Reminder: ebp holds 10.
    mul ebp
    jo free_and_exit_error

    ; Add ecx (which holds the digit in it's lowest 8 bits) to eax and check for
    ; overflows.
    add eax, ecx
    jo free_and_exit_error

    ; TODO: do the checks for overflows simultaenously by using adx.

    ; Increment rbx and repeat the loop.
    inc rbx
    jmp read_n_loop

after_read_n_loop:

    ; We move n to ebp.
    mov ebp, eax

    ; bl will hold a flag signifying whether the initial string has been read.
    mov bl, INITIAL_STRING_NOT_READ

    ; rsi will hold a pointer to the current position in the buffer. r10 will
    ; hold a pointer to the end of the read string, which is usually the end of
    ; the buffer, but not always. Both have to be initialized to the same value,
    ; so that the mechanism for reading new input into the buffer is activated.
    mov r10, rsi

    ; rdi will hold a pointer to the current position of the string we are
    ; copying over. We initialize it with the current break pointer, because we
    ; will store the strings that we are reading on the heap.
    mov rdi, r8

    ; Clear DF for the string operations we will do.
    cld

    ; We read the strings in a loop. For each string we:
    ; 1. Read the string and check it's correctness.
    ; 2a. If it's the first string, we save it as the expansion of 0x20 (space).
    ; 2b. Otherwise we store it as the expansion of it's first character.
    ; 3. Repeat the loop if it wasn't the last string.
    ; 4. After the loop, make sure that the last string is an empty string (to 
    ; make sure that there was an end-of-line after the last expansion
    ; instruction).

read_strings_loop:

    ; Save the pointer to the start of the string (stored in rdi) in r9.
    mov r9, rdi

    ; Input is read in parts that are BUFFER_LEN wide and then iterated over
    ; character by character.

read_string_loop:

    ; Save the value of rdi in r12, because rdi needs to be overridden in order
    ; to do syscalls.
    mov r12, rdi

    ; Check if the pointer to the current position in the buffer is equal to the
    ; past-the-end of the string that was read with sys_read. If yes, then
    ; continue on to the part where new input is read. Otherwise skip ahead to
    ; the part that checks the current character and copies it over.
    cmp rsi, r10
    jne after_call_sys_read

    ; Do a syscall with rax = SYS_READ = 0, edi = FD_READ = 0, rsi set to the
    ; start of the buffer and rdx = BUFFER_LEN.
    xor eax, eax
    xor edi, edi
    lea rsi, [rel buffer]
    mov rdx, BUFFER_LEN
    syscall

    ; Check the return value of read, which is stored in rax and represented as
    ; a 64-bit signed integer.
    test rax, rax

    ; If it's less than 0, then there was an error.
    jl free_and_exit_error

    ; If it's equal to 0, then sys_read gave us and empty string, which means
    ; that the end of the input has been reached and we can terminate the outer
    ; loop (read_strings_loop).
    je after_read_strings_loop

    ; Otherwise rax holds a positive integer representing the length of the
    ; non-empty string we just read. In that case we want to store the
    ; past-the-end pointer of that string in r10. This pointer is the sum of
    ; rsi and rax, because rsi currently points to the start of the buffer.
    mov r10, rsi
    add r10, rax

after_call_sys_read:

    ; Check if the pointer to the current position of the string we are copying
    ; into (currently stored in r12) is equal to the current break pointer. If
    ; yes, then we have to move the break pointer in order to make space for the
    ; string that we're copying from the buffer.
    cmp r12, r8
    jne after_move_brk

    ; We have to move brk. We set r8 to the new break pointer.
    add r8, MOVE_BRK_BLOCK_LEN

    ; We do a syscall with rax = SYS_BRK, and rdi set to the new break pointer,
    ; which is held in r8.
    mov eax, SYS_BRK
    mov rdi, r8
    syscall

    ; Now we have the new break pointer in rax. We check if it's equal to the
    ; break pointer we were expecting. If not, we treat it as a memory error.
    cmp rax, r8
    jne free_and_exit_error

after_move_brk:

    ; We are ready for checking and copying individual characters from the
    ; buffer into the string. We restore rdi with the pointer to the current
    ; position in the string we're copying over.
    mov rdi, r12
    
    ; Load the current character in the buffer pointed to by rsi into al.
    lodsb

    ; First check if the character is a new line. If it is, then exit the loop,
    ; where a null byte is appended to the string.
    cmp al, '\n'
    je after_read_string_loop

    ; If it's not a newline, then we have to check if it's a valid character.

    ; Check if the current character is above or equal to the first valid
    ; character, '!'.
    cmp al, '!'
    jb free_and_exit_error

    ; Check if the current character is below or equal to the last valid
    ; character, '~'.
    cmp al, '~'
    ja free_and_exit_error

    ; If the character is correct, we append it to the string.
    stosb

    ; Continue the loop.
    jmp read_string_loop

after_read_string_loop:

    ; The last character in the string was a new line. We want to append a null
    ; byte to the string to signify its end. To do this, we set al to 0 (null
    ; byte) and append it to the string. Note that we are zeroing rax, which
    ; useful later.
    xor eax, eax
    stosb

    ; Now we have the pointer to the null-terminated string in r9. We have to
    ; save this pointer in the expansion table. We store the first character
    ; (the character that is expanded) in al.
    mov al, [r9]

    ; Remember: the first string is special, and a flag is stored in bl. We
    ; check if it's the first string. If not, we jump to the part that deals
    ; with the following (non-initial) strings.
    test bl, bl
    jne after_initial_string

    ; If it's the initial string, then we set al (which stores the expanded
    ; character) to the space character.
    mov al, ' '

    ; The non-initial strings are stored in the expansion table as a pointer to
    ; the first character of the expansion, so the second character of the
    ; string. We want to have one logic sequence for all strings, so we
    ; decrement r9 in order to make up for how we later set the value in the
    ; expansion table to r9 + 1.
    dec r9

    ; Reset the bl flag to remember that the initial string has been read.
    xor bl, bl

after_initial_string:

    ; It's possible that the non-initial strings given in the input are empty.
    ; We check for this by making sure that the first character, which is stored
    ; in al, is not null. Note that this also works if we just read the initial
    ; string, because in that case al holds ' '.
    test al, al
    je free_and_exit_error

    ; If it's not the initial string, we set the expansions at position al to
    ; r9 + 1. First we increment r9 so that it points to the first character of
    ; the expansion string.
    inc r9

    ; We store the pointer to the buffer in rbx and add the offset represented
    ; by al.
    lea rbx, [rel buffer]
    add rbx, rax

    ; Now we move the pointer held in r9 into buffer + al.
    mov [rbx], r9

    ; Repeat the loop.
    jmp read_strings_loop

after_read_strings_loop:

    ; We read all the strings, but we still have to check if the last character
    ; was a newline. Convenietly, the pointer to the last character is held in
    ; r12 because it is transfered there right before the only place where we
    ; exit read_strings_loop. The algorithm converts newlines into null bytes
    ; to signify the end of strings, so we have to check if the character
    ; pointed to by r12 is a null byte.
    cmp [r12], 0
    jne free_and_exit_error

    ; Now we write the output. This is done with a dfs, where a character calls 
    ; dfs for each character in it's expansion, unless the recursion depth is n,
    ; in which case the character writes its expansion to the output.

    ; More specifically, dfs works like this:
    ; 1. Before dfs, the pointer to the character that is to be expanded is
    ; placed in rsi and the depth of the recursion is placed in rcx.
    ; 2. The characters pointed to by rsi are iterated over until a null byte is
    ; found.
    ; 3. For each character we save the value of rsi on the stack.
    ; 4a. If the recursion depth is equal to n, then for each character we
    ; append it to the buffer (the flushing of the buffer is described later).
    ; 4b. Otherwise for each character we save the value of rsi on the stack,
    ; call dfs and pop rsi from the stack.
    ; 5. If the recursion depth is not 0, then we jump to the part of the parent
    ; dfs where the child was called in order to give the logic flow back to the
    ; parent dfs.

    ; Initialize rsi with the pointer to space_and_null, which holds " \0", and
    ; rcx to 0, because we start with recursion depth 0.
    lea rsi, [rel space_and_null]
    xor ecx, ecx

dfs:

character_loop:

    ; Put the current character into al.
    lodsb

    cmp rcx, rbp
    jne after_write

write: