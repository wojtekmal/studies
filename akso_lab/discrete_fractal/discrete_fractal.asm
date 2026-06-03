global _start

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

ERROR_CODE equ 1

POINTER_SIZE equ 8

section .bss

; buffer is used for buffering input and output.
buffer: resb BUFFER_LEN

; expansions remembers what each character expands into. Note that expansions is
; zero initialized, which is used later in the program. We have 1 entry for each
; ascii character (so 128 in total) but only positions '!' through '~' are used.
expansions: resb 128

section .text

_start:

    ; Check that there are exactly 2 arguments: the program name and n. If not,
    ; then jump to the end of the program, where memory is freed and the program
    ; exits with an error.
    cmp QWORD [rsp], 2
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

    ; We subtract '0' from cl, so that cl stores the value represented by the
    ; digit, not the ascii encoding of the digit.
    sub cl, '0'

    ; Check if cl holds a valid digit, meaning that when treated as a 8-bit
    ; unsigned number, it is not above 9.
    cmp cl, 9
    ja free_and_exit_error

    ; We accumulate in eax in two steps: first we multiply eax by 10, then we
    ; add the digit stored in cl. Both steps require checking for overflows.

    ; Multiply n by 10 and check for overflows. Note that eax holds an unsigned
    ; 32-bit representation of n, so we use mul. Reminder: ebp holds 10.
    mul ebp
    jc free_and_exit_error

    ; Add ecx (which holds the digit in it's lowest 8 bits) to eax and check for
    ; unsigned overflows. Note that we have to check the carry flag, not the
    ; overflow flag.
    add eax, ecx
    jc free_and_exit_error

    ; Increment rbx and repeat the loop.
    inc rbx
    jmp read_n_loop

after_read_n_loop:

    ; Check if at least one digit was read by checking if rbx moved.
    cmp rbx, [rsp + 16]
    je free_and_exit_error

    ; Temporarily store n in ebx.
    mov ebx, eax

    ; TODO: change this to using the fact that brk starts at the end of dfs.
    ; Do a syscall with rax = SYS_BRK and rdi = NULLPTR = 0. sys_brk, regardless
    ; of whether it succeeds, returns the current break pointer. This means that
    ; we can tell it to set brk to nullptr and it will silently fail and return
    ; the current brk in rax.
    mov eax, SYS_BRK
    xor edi, edi
    syscall

    ; We reserve n * POINTER_SIZE bytes on the heap by moving the break pointer
    ; by that amount. This will be needed in the dfs.

    ; We save the start of the block we are reserving in r14. This is the
    ; current break pointer (currently in rax).
    mov r14, rax

    ; We set rdi to the new break pointer. This is the initial brk (held 
    ; in rax) plus n (held in rbx) times POINTER_SIZE.
    lea rdi, [rax + POINTER_SIZE * rbx]

    ; Now with rdi set, we do a syscall with rax = SYS_BRK.
    mov eax, SYS_BRK
    syscall

    ; We check that the new break pointer (stored in rax) is what we expected
    ; (still stored in rdi).
    cmp rax, rdi
    jne free_and_exit_error

    ; We store the current break pointer in r8. r8 will move along with the
    ; break pointer throughout the program. We also store it in r15. r15 will
    ; remember the end of the block we reserved.
    mov r8, rax
    mov r15, rax

    ; bl will hold a flag signifying whether the initial string has been read.
    mov bl, INITIAL_STRING_NOT_READ

    ; rsi will hold a pointer to the current position in the buffer. r10 will
    ; hold a pointer to the end of the read string, which is usually the end of
    ; the buffer, but not always. Both have to be initialized to the same value,
    ; so that the mechanism for reading new input into the buffer is activated.
    mov r10, rsi

    ; rdi will hold a pointer to the current position of the string we are
    ; copying over. Note that it already holds the current break pointer.

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
    mov edx, BUFFER_LEN
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
    lea r10, [rsi + rax]

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
    cmp al, `\n`
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

    ; Remember: the first string is special, and a flag is stored in bl. We
    ; check if it's the first string. If not, we jump to the part that deals
    ; with the following (non-initial) strings. It's a non-initial string if
    ; bl = INITIAL_STRING_READ = 0.
    test bl, bl
    je after_initial_string

    ; Reset the bl flag to remember that the initial string has been read.
    xor bl, bl

    ; Skip the part where we deal with non-initial strings.
    jmp after_non_initial_string

after_initial_string:

    ; We have the pointer to the null-terminated string in r9. We have to
    ; save this pointer in the expansion table. We store the first character
    ; (the character that is expanded) in al.
    mov al, [r9]

    ; It's possible that the non-initial strings given in the input are empty.
    ; We check for this by making sure that the first character, which is stored
    ; in al, is not null. Note that this also works if we just read the initial
    ; string, because in that case al holds ' '.
    test al, al
    je free_and_exit_error

    ; We increment r9 so that it points to the first character of the expansion
    ; string.
    inc r9

    ; We store the pointer to the expansions table in r12 and then shift it to
    ; point to the entry for the character held in al.
    lea r12, [rel expansions]
    lea r12, [r12 + POINTER_SIZE * rax]

    ; We check if there is already an expansion for the character held in al. If
    ; there is, then we end with an error. Reminder: expansions holds entries
    ; that are POINTER_SIZE, which is QWORD.
    cmp QWORD [r12], 0
    jne free_and_exit_error

    ; Now we move the pointer to the start of the string we read (held in r9)
    ; into the entry for the character held in al in the expansions table.
    mov [r12], r9

after_non_initial_string:

    ; Repeat the loop.
    jmp read_strings_loop

after_read_strings_loop:

    ; We read all the strings, but we still have to check two things: that at
    ; least one character was read and that it was a newline. We can check if at
    ; least one character was read by checking bl, which holds a flag
    ; representing whether at least one string was read. We want to check if
    ; bl != INITIAL_STRING_READ = 0.
    test bl, bl
    jne free_and_exit_error

    ; Convenietly, the pointer to the byte after the last character is held in
    ; r12 because it is transfered there right before the only place where we
    ; exit read_strings_loop. The algorithm converts newlines into null bytes to
    ; signify the end of strings, so we have to check if the character before
    ; the one pointed to by r12 is a null byte.
    cmp BYTE [r12 - 1], 0
    jne free_and_exit_error

    ; Now we write the output. This is done with a dfs, where a character calls 
    ; dfs for each character in it's expansion, unless the recursion depth is n,
    ; in which case the character writes its expansion to the output.

    ; More specifically, dfs works like this:
    ; 1. Before dfs, the pointer to the character that is to be expanded is
    ; placed in rsi and the depth of the recursion is placed in ecx.
    ; 2. The characters pointed to by rsi are iterated over until a null byte is
    ; found.
    ; 3. If the recursion depth is not 0, then we jump to the part of the parent
    ; dfs where the child was called in order to give the logic flow back to the
    ; parent dfs.

    ; Individual characters of the extension string are processed as such:
    ; 1. Save the value of rsi (pointer to the character) on the stack.
    ; 2a. If the recursion depth is equal to n, then for each character we
    ; append it to the buffer (the flushing of the buffer is described later).
    ; 2b. Otherwise we save the value of rsi on the stack,
    ; call dfs and pop rsi from the stack.
    ; 3. Pop rsi from the stack.

    ; Initialize rsi, which will point to the current character of the dfs,
    ; with the pointer to the initial string. Reminder: it is stored in r15.
    mov rsi, r15

    ; Initialize rbx to the start of the block of memory we reserved (r14) for
    ; the dfs, because it will point to the top of the artificial stack.
    mov rbx, r14

    ; Initialize rdi, which will hold the current position in the buffer, to the
    ; start of the buffer.
    lea rdi, [rel buffer]

dfs:

character_loop:

    ; Set rax to 0. This will be needed later on.
    xor eax, eax

    ; Put the current character into al and move rsi (the pointer to the current
    ; character).
    lodsb

    ; If the current character is a null byte, exit character_loop.
    test al, al
    je after_character_loop

    ; Save the pointer to the next character (held in rsi) on the artificial
    ; stack. Reminder: rbx points to the top of the stack.
    mov [rbx], rsi
    add rbx, POINTER_SIZE

    ; Set rsi to the value expected by the child dfs, which is a pointer to the
    ; expansion string of the current character, which is stored in expansions
    ; at offset al. Note that the value held in rax is equal to that held in al,
    ; because earlier we zeroed rax. Note that rsi + rax is a position in the
    ; expansions table, so we want to set rsi to the pointer that is held in the
    ; expansions table (so not rsi += 8 * rax, but rsi = *(rsi + 8 * rax)).
    lea rsi, [rel expansions]
    mov rsi, [rsi + POINTER_SIZE * rax]

    ; If the recursion depth is equal to n, or equivelantly rbx, which holds a
    ; pointer to the top of the stack, points to the end of the block of memory
    ; reserved for the stack.
    cmp rbx, r15
    je write

    ; If the entry for al in the expansion table that we just put into rsi is a
    ; nullptr (equal to 0), then write the character, because this means that
    ; this character doesn't have an entry in the expansion table.
    test rsi, rsi
    je write

expand:

    ; This is the case where the character is supposed to be expanded. In that
    ; case we need to a dfs with the expansion of the current character. Note
    ; that the current character is stored in al. We already have rsi set to the
    ; value expected by the child dfs.

    ; Do a child dfs.
    jmp dfs

after_call_child_dfs:

    ; Reminder: this is the case where we expand the character. After doing
    ; that, we need to skip past the code that is supposed to execute if the
    ; character is supposed to be expanded.
    jmp after_write

after_expand:

write:

    ; Write the current character into the current position in the buffer (held
    ; in rdi) and increment this position.
    stosb

    ; Check if we reached the end of the buffer.
    lea rdx, [rel buffer + BUFFER_LEN]
    cmp rdi, rdx
    jne after_write

    ; Flush the buffer.
    call flush_buffer

after_write:

    ; Restore rsi to the pointer to the next character, regardless of which case
    ; we have: writing the character or expanding it. We do this by popping a
    ; value from the artificial stack. Reminder: rbx points to the top of the
    ; stack.
    sub rbx, POINTER_SIZE
    mov rsi, [rbx]

    ; Do another iteration of the loop of the characters in the expansion.
    jmp character_loop

after_character_loop:

    ; If the recursion depth isn't 0, we return to the parent. We check this by
    ; checking if the top of the stack (rbx) is equal to the start of the memory
    ; block reserved for the stack (r14).
    cmp rbx, r14
    jne after_call_child_dfs

after_dfs:

    ; Add a newline to the buffer. Reminder: rdi points to the current position
    ; (past-the-end of the contents) of the buffer.
    mov al, `\n`
    stosb

    ; Flush the buffer one last time.
    call flush_buffer

    ; Zero rdi. This will be needed in a moment.
    xor edi, edi

    ; There was no error, so we skip the return error part.
    jmp after_free_and_exit_error

free_and_exit_error:
    
    ; Set edi to the value we want to return in the event of an error.
    mov edi, ERROR_CODE

after_free_and_exit_error:

    ; Now rdi is set to the return code of out program (it was zeroed and set to
    ; ERROR_CODE in the event of an error). We do a syscall with rax = SYS_EXIT.
    mov eax, SYS_EXIT
    syscall

    
    ; Here we have the flush_buffer helper function.

flush_buffer:

    ; rdx holds the past-the-end of the buffer's contents.
    
    ; We do a syscall with rax = SYS_WRITE, rdi = FD_WRITE, rsi = rbx (start of
    ; the buffer), and rdx set to the length of the buffer, which is the current
    ; past-the-end minus the start of the buffer (which is moved to rsi in the
    ; code below). Reminder: rdi is already set to FD_WRITE.
    mov eax, SYS_WRITE
    lea rsi, [rel buffer]
    mov rdx, rdi
    sub rdx, rsi
    mov rdi, FD_WRITE
    syscall

    ; Now rax holds the return value of sys_write. sys_write returns either an
    ; error, which is negative, or the number of characters written. We can
    ; check if the write was correct by checking if exactly BUFFER_LEN
    ; characters were written.
    cmp rax, rdx
    jne free_and_exit_error

    ; We set rdx to the new past-the-end of the buffer's contents, which is the
    ; start of the buffer.
    lea rdi, [rel buffer]

    ret