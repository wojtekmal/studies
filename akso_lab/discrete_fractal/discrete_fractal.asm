global _start

SYS_READ equ 0
SYS_WRITE equ 1
SYS_BRK equ 12
SYS_EXIT equ 60

FD_READ equ 0
FD_WRITE equ 1

BUFFER_LEN equ 4096
BUFFER_LEN_DIVIDED_BY_256 equ 16

INITIAL_STRING_NOT_READ equ 1
INITIAL_STRING_READ equ 0

NULLPTR equ 0

MOVE_BRK_BLOCK_LEN equ 4096

NO_ERROR_CODE equ 0
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

    ; We have some spare registers: rbp, r11, r13. r11 is modified by syscalls,
    ; but rbp and r13 are safe. This means that we can use them for holding
    ; values that would otherwise be calculated many times. We chose to use
    ; them for holding the rip-relative addresses of the expansions and buffer
    ; arrays.
    lea rbp, [rel expansions]
    lea r13, [rel buffer]

    ; Check that there are exactly 2 arguments: the program name and n. If not,
    ; then jump to the end of the program, where memory is freed and the program
    ; exits with an error. The number of arguments is pointed to by rsp. This
    ; means that we can pop the value (rax was chosen arbitrarily).
    pop rax
    cmp rax, 2
    jne jump_to_exit_error

    ; We pop into an arbitrary 8 byte register in order to move rsp by 8. This
    ; could have been add rsp, 8, except that that would have a longer opcode.
    ; We do this in order to move rsp to the pointer to the second argument of
    ; the program.
    pop rax

    ; Store the pointer to the first byte of the second argument in rsi. We just
    ; moved rsp, so now all we have to do is pop into rsi.
    pop rsi

    ; Now rsi points to the first digit of n. We will accumulate the unsigned
    ; 64-bit value of n in rbx. For each digit, we:
    ; 1. Breaking the loop if we found a null byte.
    ; 2. Check if it's a digit.
    ; 3. Accumulate in rbx.
    ; 4. Check if there was an overflow.

    ; Start with zeroing rax and rbx, because they will be used to store the
    ; current digit and n, respectively. We also zero rdx, which will be used
    ; to hold a flag representing whether there was at least one character in
    ; the decimal representation of n. For reasons that will become clear later,
    ; this flag is 0 when no characters are found and BUFFER_LEN when at least
    ; one character is found. After zeroing eax we can sign-extend eax into 64
    ; bits, which zeroes edx and is one by shorter than xor edx, edx.
    xor eax, eax
    xor ebx, ebx
    cdq

read_n_loop:
    ; Store the digit (one byte) in al and move rsi forward.
    lodsb

    ; Check if it's a null byte. If yes, then break the loop.
    test al, al
    je after_read_n_loop

    ; Set the flag (rdx) to BUFFER_LEN. Note that rdx is zero, and
    ; BUFFER_LEN happens to be a multiple of 256 that happens to fit in dx, so
    ; we only have to change dh.
    mov dh, BUFFER_LEN_DIVIDED_BY_256

    ; We subtract '0' from al, so that al stores the value represented by the
    ; digit, not the ascii encoding of the digit.
    sub al, '0'

    ; Check if al holds a valid digit, meaning that when treated as a 8-bit
    ; unsigned number, it is not above 9. Note that we don't have to check if
    ; the ascii encoding was below that of 0, because those values were wrapped
    ; around when we subtracted '0'.
    cmp al, 9
    ja jump_to_exit_error

    ; We accumulate in rbx: first we multiply it by 10, then we add the digit
    ; stored in al.

    ; Multiply n by 10. Note that the valid values of n fit in the first 32 bits
    ; of rbx, so we can use imul and not worry about signdness because we are
    ; using a 64 bit register for holding n. We use imul instead of mul because
    ; mul doesn't have a 2 argument variant.
    imul rbx, 10

    ; Add rax (which holds the digit in it's lowest 8 bits) to rbx.
    add rbx, rax

    ; Check if there was an overflow by checking if the value held in rbx fits
    ; in the first 32 bits. We do this by copying the current value of n into
    ; an auxiliary register, shifting that register 32 bits to the right (which
    ; leaves only the high bits), and checking if it's 0.
    mov rcx, rbx
    shr rcx, 32
    jnz jump_to_exit_error

    ; Repeat the loop.
    jmp read_n_loop

after_read_n_loop:

    ; Check if at least one digit was read by checking the flag. Note that its
    ; enough to check dh, because the only possible values of the flag (rdx) are
    ; 0 and BUFFER_LEN, which happens to have bits set in dh.
    test dh, dh
    jz jump_to_exit_error

    ; There are two ways to find the current break pointer: calculate it as the
    ; end of .bss, aligned to the page size, or what we chose to do, which is to
    ; do a syscall with rax = SYS_BRK and rdi = NULLPTR = 0. sys_brk, regardless
    ; of whether it succeeds, returns the current break pointer. This means that
    ; we can tell it to set brk to nullptr and it will silently fail and return
    ; the current brk in rax. Note that the higher bits of rax where zeroed
    ; earlier, and SYS_BRK fits in 8 bits, so we can set al to SYS_BRK.
    mov al, SYS_BRK
    xor edi, edi
    syscall

    ; We reserve (n + 1) * POINTER_SIZE bytes on the heap by moving the break
    ; pointer by that amount. The reason why we are doing this will become clear
    ; later.

    ; We save the start of the block we are reserving in r14. This is the
    ; current break pointer (currently in rax, because we just called sys_brk
    ; with rdi = NULLPTR).
    mov r14, rax

    ; We set rdi to the new break pointer. This is the initial brk (rax) plus
    ; n + 1 times POINTER_SIZE. Note that rbx holds n, so its easier to do
    ; brk + POINTER_SIZE * n + POINTER_SIZE instead of 
    ; brk + POINTER_SIZE * (n + 1).
    lea rdi, [rax + POINTER_SIZE * rbx + POINTER_SIZE]

    ; Now with rdi set to the new break pointer, we do a syscall with
    ; rax = SYS_BRK. Note that the opcode for what's below is one byte shorter
    ; than the opcode for mov eax, SYS_BRK (which is still shorter than using
    ; rax instead of eax).
    xor eax, eax
    mov al, SYS_BRK
    syscall

    ; We check that the new break pointer (stored in rax) is what we expected
    ; (still stored in rdi - syscalls preserve the values of most registers).
    ; If it is, then we skip the part that exits with an error.
    cmp rax, rdi
    je after_jump_to_exit_error

    ; Unfortunately, jumping more than 128 bytes either way turns a 2 byte jump
    ; opcode into a 5 byte opcode. We jump to exit_error many times, and not all
    ; jumps to that place are in the range for a 2 byte opcode, so we add a
    ; single label with a jump to exit_error. Now every place where we want to
    ; jump to exit_error is either within range of jump_to_exit_error or
    ; exit_error. These will be jumped to whenever we want to exit with an
    ; error. Also the instruction below is within a 128 byte range of exit
    ; error, which saves another 3 bytes.
jump_to_exit_error:

    jmp exit_error

after_jump_to_exit_error:

    ; We store the current break pointer in r8. r8 will move along with the
    ; break pointer throughout the program. We also store it in r15. r15 will
    ; remember the end of the block we reserved.
    mov r8, rax
    mov r15, rax

    ; bl will hold a flag signifying whether the initial string has been read.
    mov bl, INITIAL_STRING_NOT_READ

    ; rsi will hold a pointer to the current position in the buffer. r10 will
    ; hold a pointer to the end of the string we read into the buffer, which is
    ; usually the end of the buffer, but not always. Both have to be initialized
    ; to the same value, so that the mechanism for reading new input into the
    ; buffer is activated.
    mov r10, rsi

    ; rdi will hold a pointer to the current position of the string we are
    ; copying over. Note that it already holds the current break pointer, which
    ; is convenient because we will be putting the strings from the input onto
    ; the heap, so rdi already points to where we will put the first character.

    ; We read the strings in a loop. For each string we:
    ; 1. Read the string and check its correctness.
    ; 2. Exit the loop if sys_read gives us 0 characters.
    ; 3a. If it's the initial string, we zero the initial string flag (bl).
    ; 3b. Otherwise we store it as the expansion of it's first character.
    ; 4. Repeat the loop.

read_strings_loop:

    ; Save the pointer to the start of the string (stored in rdi) in r9.
    mov r9, rdi

    ; Input is read in parts that are BUFFER_LEN wide and then iterated over
    ; character by character.

read_string_loop:

    ; Save the value of rdi (pointer to the string we are reading) in r12,
    ; because rdi needs to be overwritten in order to do syscalls.
    mov r12, rdi

    ; Check if the pointer to the current position in the buffer (rsi) is equal
    ; to the end of the buffered string (r10). If yes, then continue on to the
    ; part where new input is read. Otherwise skip ahead to the part that checks
    ; the current character and copies it over.
    cmp rsi, r10
    jne after_call_sys_read

    ; Do a syscall with rax = SYS_READ = 0, edi = FD_READ = 0, rsi set to the
    ; start of the buffer (r13) and rdx = BUFFER_LEN. Note that rdx is already
    ; set to BUFFER_LEN, because it was used as a flag and the only non-error
    ; value of this flag was BUFFER_LEN.
    xor eax, eax
    xor edi, edi
    mov rsi, r13
    syscall

    ; Check the return value of sys_read, which is stored in rax and represented
    ; as a 64-bit signed integer.
    test rax, rax

    ; If it's less than 0, then there was an error.
    jl exit_error

    ; If it's equal to 0, then sys_read gave us and empty string, which means
    ; that the end of the input has been reached and we can terminate the outer
    ; loop (read_strings_loop).
    je after_read_strings_loop

    ; Otherwise rax holds a positive integer representing the length of the
    ; non-empty string we just read. In that case we want to store the pointer
    ; to the end of that string in r10. This pointer is the sum of rsi (start of
    ; the buffer) and rax (length of string that was read).
    lea r10, [rsi + rax]

after_call_sys_read:

    ; Check if the pointer to the current position of the string we are copying
    ; into (currently stored in r12, because we are storing it here temporarily
    ; instead of holding it in rdi) is equal to the current break pointer (r8).
    ; If yes, then we have to move the break pointer in order to make space for
    ; the string that we're copying from the buffer.
    cmp r12, r8
    jne after_move_brk

    ; We have to move the break pointer. The current value is stored in r8. We
    ; set r8 to the new break pointer, which is the old value plus
    ; MOVE_BRK_BLOCK_LEN. Note that MOVE_BRK_BLOCK_LEN = BUFFER_LEN and that
    ; rdx holds BUFFER_LEN throughout the loop (because it was set to this value
    ; as a flag when reading n), which means that in order to update r8, we can
    ; just add rdx.
    add r8, rdx

    ; We do a syscall with rax = SYS_BRK, and rdi set to the new break pointer,
    ; which is held in r8. As before, the below code is shorter than
    ; mov eax, SYS_BRK.
    xor eax, eax
    mov al, SYS_BRK
    mov rdi, r8
    syscall

    ; Now we have the new break pointer in rax. We check if it's equal to the
    ; break pointer we were expecting (r8). If not, we treat it as an OOM error.
    cmp rax, r8
    jne exit_error

after_move_brk:

    ; We are ready for checking and copying individual characters from the
    ; buffer into the string. We restore rdi with the pointer to the current
    ; position in the string that we're copying into (temporarily held in r12).
    mov rdi, r12
    
    ; Load the current character in the buffer pointed to by rsi into al and
    ; increment rsi.
    lodsb

    ; First check if the character is a newline. If it is, then exit the inner
    ; loop, because this is the end of the string.
    cmp al, `\n`
    je after_read_string_loop

    ; If it's not a newline, then we have to check if it's a valid character.

    ; Check if the current character is below the first valid character, '!'.
    cmp al, '!'
    jb exit_error

    ; Check if the current character is above the last valid character, '~'.
    cmp al, '~'
    ja exit_error

    ; If the character is correct, we append it to the string. Note that rdi
    ; points to the current end of the string, so after stosb it moves by one
    ; byte and still points to the end of the string.
    stosb

    ; Continue the inner loop.
    jmp read_string_loop

after_read_string_loop:

    ; The last character in the string was a new line. We want to append a null
    ; byte to the string (instead of a newline) to signify its end. To do this,
    ; we set al to 0 (null byte) and append it to the end of the string (rdi).
    ; Note that we are also zeroing rax, which is needed later.
    xor eax, eax
    stosb

    ; Remember: the first string is special, and a flag is stored in bl. We
    ; check if it's the first string. If not, we jump to the part that deals
    ; with the non-initial strings. It's a non-initial string if
    ; bl = INITIAL_STRING_READ = 0.
    test bl, bl
    jne after_non_initial_string

    ; We have the pointer to the null-terminated string in r9. We have to
    ; save this pointer in the expansion table. We store the first character
    ; (the character that is expanded) in al.
    mov al, [r9]

    ; It's possible that the non-initial strings given in the input are empty.
    ; We check for this by making sure that the first character, which is stored
    ; in al, is not a null byte.
    test al, al
    je exit_error

    ; We increment r9 so that it points to the first character of the expansion
    ; string (so the string we read, but not including the first character).
    inc r9

    ; We store the pointer to the expansions table (rbp) at offset equal to the
    ; ascii code of the character we are expanding (al), in r12.
    lea r12, [rbp + POINTER_SIZE * rax]

    ; We exchange the values of the entry for the expansion of the character
    ; (pointed to by r12) and the pointer to the expansion string (r9). This
    ; does two things. Firstly, it saves the pointer to the expansion of the
    ; character in the expansions table. Secondly, it puts the old value of the
    ; entry in the expansions table in r9.
    xchg [r12], r9

    ; Since we have the old entry in r9, we can check if its equal to NULLPTR.
    ; It should be, because otherwise the character already had an expansion
    ; string (so there are two expansion strings for one character, in which
    ; case we exit with an error).
    test r9, r9
    jnz exit_error

after_non_initial_string:

    ; Set the bl flag to INITIAL_STRING_READ = 0 to remember that at least one
    ; string has been read.
    xor bl, bl

    ; Repeat the outer loop.
    jmp read_strings_loop

after_read_strings_loop:


    ; We read all the strings, but we still have to check two things: that at
    ; least one character was read and that the last one was a newline. We can
    ; check if at least one character was read by checking bl, which holds a
    ; flag representing whether at least one string was read. We want to check
    ; if bl = INITIAL_STRING_READ = 0.
    test bl, bl

    ; If bl = INITIAL_STRING_READ, then we skip the part that exits with an
    ; error. This part is placed here for the same reason stated at the start of
    ; the program: so that all places where we want to exit with an error are
    ; within a 128 byte range of either jump_to_exit_error or exit_error.
    je after_exit_error

exit_error:
    
    ; Set rdi to the value we want to return in the event of an error, which is
    ; ERROR_CODE = 1. Note that the opcode of the instructions below is 1 byte
    ; shoter than the opcode for mov edi, ERROR_CODE.
    xor edi, edi
    inc edi
    jmp exit

after_exit_error:

    ; Convenietly, the pointer to the byte after the last character is held in
    ; r12 because it is transfered there right before the only place where we
    ; exit read_strings_loop. The algorithm converts newlines into null bytes to
    ; signify the end of strings, so we have to check if the character before
    ; the one pointed to by r12 is a null byte in order to check if the last
    ; character was a newline.
    cmp BYTE [r12 - 1], 0
    jne exit_error

    ; Now we write the output. This is done with a dfs, where a character calls 
    ; dfs for each character in it's expansion, unless the recursion depth is n,
    ; or it doesn't have an expansion, in which case the character writes its
    ; expansion to the output. The pointers to the characters we are iterating
    ; over in consecutive levels of the dfs are stored in consecutive places in
    ; the block of memory of size (n + 1) * POINTER_SIZE we reserved at the
    ; beginning. This block of memory is an artificial stack, sometimes called
    ; simply "the stack". We don't use the regular stack (pointed to by rsp)
    ; because we might do a recursion of depth n, which normally wouldn't fit
    ; in the stack limit.

    ; More specifically, dfs works like this:
    ; 1. Before dfs, the pointer to the character that is to be expanded is
    ; placed in rsi and the pointer to the position in the stack is placed in
    ; rbx.
    ; 2. The characters pointed to by rsi are iterated over until a null byte is
    ; found.
    ; 3. If the recursion depth is not 0, then we jump to the part of the parent
    ; dfs where the child was called in order to give the logic flow back to the
    ; parent dfs.

    ; Individual characters of the extension string are processed as such:
    ; 1. Save the value of rsi (pointer to the character) on the stack.
    ; 2a. If the recursion depth is equal to n or if there is no expansion, then
    ; for each character we append it to the buffer (the flushing of the buffer
    ; is described later).
    ; 2b. Otherwise we save the value of rsi on the stack, call a child dfs and
    ; pop rsi from the stack.
    ; 3. Pop rsi from the stack.

    ; Initialize rsi, which will point to the current character of the dfs,
    ; with the pointer to the initial string (r15).
    mov rsi, r15

    ; Initialize rbx to the start of the block of memory we reserved (r14) for
    ; the stack, because it will point to the top of the stack.
    mov rbx, r14

    ; Initialize rdi, which will hold the current position in the buffer, to the
    ; start of the buffer (r13).
    mov rdi, r13

dfs:

character_loop:

    ; Set rax to 0. This will be needed later on.
    xor eax, eax

    ; Put the current character into al and move rsi (the pointer to the current
    ; character).
    lodsb

    ; If the current character is a null byte, exit character_loop, because we
    ; finished the string at the current level of the dfs.
    test al, al
    je after_character_loop

    ; Save the pointer to the next character (rsi) on the stack. Reminder: rbx
    ; points to the top of the stack, so we have to shift it so that it points
    ; to the new top of the stack.
    mov [rbx], rsi
    add rbx, POINTER_SIZE

    ; Set rsi to the value expected by the child dfs, which is a pointer to the
    ; expansion string of the current character, which is stored in expansions
    ; at offset al. Note that the value held in rax is equal to that held in al,
    ; because earlier we zeroed rax. Note that rsi + rax is a position in the
    ; expansions table, so we want to set rsi to the pointer that is held in the
    ; expansions table (so not rsi += 8 * rax, but rsi = *(rsi + 8 * rax)).
    mov rsi, [rbp + POINTER_SIZE * rax]

    ; If the recursion depth is equal to n, or equivelantly rbx, which holds a
    ; pointer to the top of the stack, points to the end of the block of memory
    ; reserved for the stack (r15), then we write the character.
    cmp rbx, r15
    je write

    ; If the entry for the character in the expansion table that we just put
    ; into rsi is a nullptr (equal to 0), then write the character, because
    ; this means that this character doesn't have an entry in the expansion
    ; table.
    test rsi, rsi
    je write

    ; If we don't write the character, then we expand it.

expand:

    ; This is the case where the character is supposed to be expanded. In that
    ; case we need to a dfs with the expansion of the current character. Note
    ; that the current character is stored in al. We already have the pointer to
    ; the character (rsi) and the top of the stack (rbx) set to the values
    ; expected by the child dfs. We run a child dfs.
    jmp dfs

    ; Leave a label so that the child dfs knows where to return.

after_call_child_dfs:

    ; Reminder: this is the case where we expand the character. After doing
    ; that, we need to skip past the code that is supposed to execute if the
    ; character is supposed to be written.
    jmp after_write

after_expand:

write:

    ; Write the current character (al) into the current position in the buffer
    ; (rdi) and increment this position.
    stosb

    ; Check if we reached the end of the buffer, so if the end of the buffer's 
    ; contents (rdi) is equal to the start of the buffer (r13) plus it's length.
    ; Here rdx is chosen as an auxiliary register. If not, then skip
    ; the call to flush_buffer.
    lea rdx, [r13 + BUFFER_LEN]
    cmp rdi, rdx
    jne after_write

    ; Flush the buffer.
    call flush_buffer

after_write:

    ; Restore rsi to the pointer to the next character, regardless of which case
    ; we have: writing the character or expanding it. We do this by popping a
    ; value from the stack. Reminder: rbx points to the top of the stack, so we
    ; have to shift it back.
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

    ; Add a newline to the buffer. Reminder: rdi points to the end of the
    ; contents of the buffer.
    mov al, `\n`
    stosb

    ; Flush the buffer one last time.
    call flush_buffer

    ; Set rdi to NO_ERROR_CODE = 0, because there was no error and rdi holds the
    ; return value when doing sys_exit.
    xor edi, edi

exit:

    ; Now rdi is set to the return code of the program (it was zeroed and set to
    ; ERROR_CODE in the event of an error). We do a syscall with rax = SYS_EXIT.
    ; As before, the below instructions have a shorter opcode than
    ; mov eax, SYS_EXIT.
    xor eax, eax
    mov al, SYS_EXIT
    syscall

    
    ; Here we have the flush_buffer helper function.

flush_buffer:

    ; rdi holds the end of the buffer's contents.
    
    ; We do a syscall with rax = SYS_WRITE, rdi = FD_WRITE, rsi set to the start
    ; of the buffer (r13), and rdx set to the length of the buffer, which is the
    ; current end of the contents minus the start of the buffer (which is moved
    ; to rsi in the code below).
    
    ; Set eax to SYS_WRITE.
    xor eax, eax
    mov al, SYS_WRITE

    ; Set rsi to the start of the buffer (r13).
    mov rsi, r13

    ; Set rdx to rdi - rsi.
    mov rdx, rdi
    sub rdx, rsi

    ; Set rdi to FD_WRITE = 1.
    xor edi, edi
    inc edi
    
    syscall

    ; Now rax holds the return value of sys_write. sys_write returns either an
    ; error, which is negative, or the number of characters written. We can
    ; check if the write was correct by checking if exactly BUFFER_LEN
    ; characters were written.
    cmp rax, rdx
    jne exit_error

    ; We set rdi to the new past-the-end of the buffer's contents, which is the
    ; start of the buffer (r13).
    mov rdi, r13

    ret