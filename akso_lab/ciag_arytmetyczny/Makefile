test:
	clang -Wall -o arithmetic_sequence.o arithmetic_sequence.c -Oz -c
	gcc -c -Wall -Wextra -std=c23 -O2 -o arithmetic_sequence_example.o arithmetic_sequence_example.c
	gcc -o arithmetic_sequence_example arithmetic_sequence_example.o arithmetic_sequence.o
	./arithmetic_sequence_example

test_asm:
	nasm -f elf64 -w+all -w+error -o arithmetic_sequence.o arithmetic_sequence.asm -g
	gcc -c -Wall -Wextra -std=c23 -O2 -o arithmetic_sequence_example.o arithmetic_sequence_example.c -g
	gcc -o arithmetic_sequence_example arithmetic_sequence_example.o arithmetic_sequence.o -g
	./arithmetic_sequence_example