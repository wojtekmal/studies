================================
C Programming Notes for C++ Users
Exam Focus: C17, Algorithms, Data Structures
================================

A. CORE C HEADERS & FUNCTIONS
-----------------------------

Since you're writing on paper, always remember to `#include` the correct headers at the top of your file.

### 1. <stdio.h> (Standard Input/Output)

This is for "printing" to the screen and "reading" from the keyboard.

* `int printf(const char *format, ...);`
    * Used for formatted output.
    * Returns the number of characters printed or a negative value on error.
    * **Example:**
      ```c
      printf("Age: %d, Name: %s\n", 25, "Bob");
      ```
    * **Common Specifiers:**
        * `%d` or `%i`: Signed integer (int)
        * `%u`: Unsigned integer (unsigned int)
        * `%ld`: Long integer (long int)
        * `%zu`: `size_t` type (VERY common in C for sizes/indices)
        * `%f`: Floating point (double)
        * `%c`: Character (char)
        * `%s`: String (null-terminated `char*`)
        * `%p`: Pointer (prints address)
        * `%%`: Prints a literal '%' sign

* `int scanf(const char *format, ...);`
    * Used for formatted input.
    * Returns the number of items successfully read.
    * **CRITICAL C vs C++ GOTCHA:** `scanf` needs **pointers** to the variables it should fill. This is the #1 mistake.
    * **Example:**
      ```c
      int age;
      char name[50];
      printf("Enter age and name: ");
      // 'name' is already a pointer (an array decays to a pointer), so no '&'
      // '%49s' is a security measure to prevent buffer overflow
      int result = scanf("%d %49s", &age, name); // <-- Notice '&age'
      ```

### 2. <stdlib.h> (Standard Library)

This is for memory management, string-to-number conversions, and program control.

* `void* malloc(size_t size);`
    * Allocates `size` bytes of **uninitialized** memory.
    * Returns a `void*` pointer to the new memory, or `NULL` on failure.
    * **You must always check for `NULL`.**
    * **You must `free()` this memory later.**
    * **Example:**
      ```c
      int* arr = (int*)malloc(10 * sizeof(int));
      ```
        * Note: In C, the cast `(int*)` is optional, but good practice. In C++, it's required.

* `void* calloc(size_t num, size_t size);`
    * Allocates memory for an array of `num` elements, each `size` bytes long.
    * **Difference from `malloc`:** Memory is **zero-initialized**.
    * **Example:**
      ```c
      int* arr = (int*)calloc(10, sizeof(int)); // All 10 ints are 0
      ```

* `void* realloc(void* ptr, size_t new_size);`
    * Resizes a memory block previously allocated by `malloc` or `calloc`.
    * `ptr` is the old pointer. `new_size` is the new total size in bytes.
    * Returns a pointer to the resized memory (which might be at a new location) or `NULL` on failure.
    * **If it returns a new pointer, the old `ptr` is automatically freed.**
    * **Common pattern:**
      ```c
      int* new_arr = (int*)realloc(arr, 20 * sizeof(int));
      if (new_arr == NULL) {
          // Handle error, 'arr' is still valid
          free(arr);
      } else {
          arr = new_arr; // Update pointer
      }
      ```

* `void free(void* ptr);`
    * Deallocates a block of memory previously allocated by `malloc`, `calloc`, or `realloc`.
    * Passing `NULL` to `free` is safe and does nothing.
    * **Calling `free` on the same pointer twice is undefined behavior (double free).**

* `int atoi(const char* str);`
    * Converts a string (`char*`) to an integer (`int`). (Stands for "ASCII to integer").

* `void exit(int status);`
    * Terminates the program. `exit(0)` or `exit(EXIT_SUCCESS)` means success. `exit(EXIT_FAILURE)` means error.

### 3. <string.h> (String/Memory Functions)

This is for *raw memory manipulation*. These functions do not know about data types; they just move bytes.

* `void* memset(void* dest, int c, size_t count);`
    * Fills the first `count` bytes of the memory area pointed to by `dest` with the constant byte `c`.
    * **Use case:** Zeroing out a struct or array.
    * **Example:**
      ```c
      struct MyStruct s;
      memset(&s, 0, sizeof(struct MyStruct)); // Zeros the whole struct
      ```

* `void* memcpy(void* dest, const void* src, size_t count);`
    * Copies `count` bytes from `src` to `dest`.
    * **Important:** The memory areas **must not overlap**. Use `memmove` (also in `<string.h>`) if they might.
    * **Example:**
      ```c
      int src[] = {1, 2, 3};
      int dest[3];
      memcpy(dest, src, 3 * sizeof(int));
      ```

* `size_t strlen(const char* str);`
    * Returns the length of a string, **not** including the null-terminator (`\0`).

* `int strcmp(const char* s1, const char* s2);`
    * Compares two strings.
    * Returns:
        * `< 0` if s1 is less than s2
        * `0` if s1 is equal to s2
        * `> 0` if s1 is greater than s2

### 4. Other Important Headers

* `<stddef.h>`:
    * Provides `size_t` (the type returned by `sizeof` and used for all memory function sizes).
    * Provides `NULL` (a null pointer constant).
* `<stdbool.h>`:
    * Provides `bool`, `true`, and `false`. (C++ has these built-in; C does not).
    * **Always include this** if you want to use `bool`. Otherwise, you must use `int` (0 for false, non-zero for true).
* `<stdint.h>`:
    * Provides fixed-width integer types like `int32_t`, `uint32_t`, `int64_t`. Very useful for algorithms to guarantee size.
* `<assert.h>`:
    * Provides `assert(expression)`. If the expression is false, the program aborts.
    * Excellent for checking pre-conditions and invariants (e.g., `assert(ptr != NULL)`).

-----------------------------
B. C vs. C++: KEY DIFFERENCES & GOTCHAS
-----------------------------

* **No `new`/`delete`:** You **must** use `malloc`/`free`.
* **Structs:** In C, you must write `struct MyStruct s;`. In C++, you can just write `MyStruct s;`.
    * **C idiom:** Use `typedef` to mimic C++ behavior.
      ```c
      typedef struct Node {
          int data;
          struct Node* next;
      } Node;

      Node* n = (Node*)malloc(sizeof(Node)); // Now you can just use 'Node'
      ```
* **No Pass-by-Reference (`&`)**: C only has pass-by-value. To "pass by reference," you must explicitly pass a pointer.
    ```c
    // C++
    // void increment(int& x) { x++; }
    // int a = 5; increment(a);

    // C equivalent
    void increment(int* x_ptr) {
        (*x_ptr)++; // Dereference the pointer to change the original value
    }
    int a = 5;
    increment(&a); // Pass the address
    ```
* **Strings:** C has no `std::string`. Strings are `char*` (pointers to a character) that are terminated by a null byte (`\0`). You are responsible for all memory management.
* **Boolean:** Use `<stdbool.h>` for `bool`.
* **No Function Overloading:** You cannot have two functions with the same name, even with different parameters.

-----------------------------
C. COMPILER FLAGS EXPLAINED
-----------------------------

You must write code that satisfies all these flags. `-Werror` means *any warning* will stop compilation.

* `-std=c17`: Use the C17 standard.
* `-pedantic`: Be extra strict about the standard.
* `-Wall`, `-Wextra`: Enable all standard and extra warnings. This is the source of most errors.
* `-Wformat-security`: Warns if `printf`/`scanf` format strings are mismatched with arguments.
* `-Wduplicated-cond`: Warns for `if (x > 5 && x > 5)`.
* `-Wfloat-equal`: Warns if you use `==` or `!=` with floats (due to precision issues).
* `-Wshadow`: Warns if a local variable has the same name as a global or outer-scope variable.
* `-Wconversion`: Warns about implicit conversions that might lose data (e.g., `int x = 5.5;`).
* `-Wjump-misses-init`: Warns if a `goto` or `switch` jumps over a variable's initialization.
* `-Wlogical-not-parentheses`: Warns for `!a && b`, which might be ambiguous. Prefers `(!a) && b`.
* `-Wnull-dereference`: Tries to warn if it detects a `NULL` pointer being used.
* `-Wvla`: Warns on Variable Length Arrays (e.g., `int n = 10; int arr[n];`).
    * **CRITICAL:** This flag plus `-Werror` **bans VLAs**. You **must** use `malloc` for dynamic-sized arrays.
* `-Werror`: **Treats all warnings as errors.** This is the key. Your code must be 100% warning-free.

* (Runtime/Debug Flags):
* `-fstack-protector-strong`: Adds security checks to prevent stack buffer overflows.
* `-fsanitize=undefined`: Turns on the Undefined Behavior Sanitizer (UBSan). Your code will crash if you do things like integer overflow, divide by zero, etc.
* `-fno-sanitize-recover`: Tells UBSan to crash immediately, not try to continue.
* `-g`: Include debug symbols.
* `-fno-omit-frame-pointer`: Keep debug information (stack frames) for better debugging.
* `-O1`: Level 1 optimization.

-----------------------------
D. PROBLEMATIC CODE PATTERNS (due to -Werror)
-----------------------------

Any of these will likely cause a compilation error.

1.  **Using VLAs (Variable Length Arrays)**
    * **BAD:**
      ```c
      int n; scanf("%d", &n); int my_array[n];
      ```
    * **FIX:**
      ```c
      int n; scanf("%d", &n);
      int* my_array = (int*)malloc(n * sizeof(int)); // (And check for NULL!)
      ```

2.  **Implicit Conversions (`-Wconversion`)**
    * **BAD:**
      ```c
      int x = 5.5; // (double to int, loses data)
      char c = 1000; // (int to char, overflows)
      ```
    * **FIX:**
      ```c
      int x = (int)5.5;
      ```

3.  **Signed/Unsigned Mismatch (`-Wall`)**
    * `size_t` (from `sizeof`) is unsigned.
    * **BAD:**
      ```c
      for (int i = 0; i < strlen(s); i++)
      ```
    * **FIX:**
      ```c
      for (size_t i = 0; i < strlen(s); i++)
      ```
    * **Very Bad:**
      ```c
      int x = -1;
      size_t y = 5;
      if (x < y) { /* This is FALSE! */ }
      ```
    * **FIX:**
      ```c
      int x = -1;
      size_t y = 5;
      if (x < (int)y) { /* This is TRUE */ }
      ```

4.  **`scanf` without Pointers (`-Wformat-security`)**
    * **BAD:**
      ```c
      int x; scanf("%d", x); // (Will crash or worse)
      ```
    * **FIX:**
      ```c
      int x; scanf("%d", &x);
      ```

5.  **Unused Variables (`-Wall`)**
    * **BAD:**
      ```c
      int my_func() { int x = 5; return 0; } // 'x' is unused
      ```
    * **FIX:**
      ```c
      int my_func() { int x = 5; (void)x; return 0; } // Or just remove it
      ```

6.  **Comparing Floats (`-Wfloat-equal`)**
    * **BAD:**
      ```c
      double a = 0.1, b = 0.2;
      if (a + b == 0.3) { /* This is FALSE! */ }
      ```
    * **FIX:**
      ```c
      #include <math.h> // for fabs
      double epsilon = 0.00001;
      if (fabs((a + b) - 0.3) < epsilon) { /* This is TRUE */ }
      ```

7.  **Variable Shadowing (`-Wshadow`)**
    * **BAD:**
      ```c
      int x = 10;
      for (int i = 0; i < 5; i++) {
          int x = i; // This 'x' shadows the outer 'x'
          printf("%d", x);
      }
      ```
    * **FIX:**
      ```c
      int x = 10;
      for (int i = 0; i < 5; i++) {
          int inner_x = i;
          printf("%d", inner_x);
      }
      ```

8.  **Missing `switch` Cases (`-Wall`)**
    * **BAD:**
      ```c
      enum State { A, B };
      enum State s = A;
      switch (s) {
          case A: break;
      } // <-- Missing 'case B:' and 'default:'
      ```
    * **FIX:**
      ```c
S      switch (s) {
          case A: break;
          case B: break;
          default:
              break;
      }
      ```

9.  **Using Uninitialized Variables**
    * **BAD:**
      ```c
      int x;
      if (1 < 2) {
          x = 10;
      }
      printf("%d", x); // Error: 'x' might be uninitialized
      ```
    * **FIX:**
      ```c
      int x = 0; // Always initialize
      if (1 < 2) {
          x = 10;
      }
      printf("%d", x);
      ```