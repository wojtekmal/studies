arithmetic_sequence.o:
	clang -Wall -o arithmetic_sequence.o arithmetic_sequence.c -Oz -c

test: arithmetic_sequence.o
	gcc -c -Wall -Wextra -std=c23 -O2 -o arithmetic_sequence_example.o arithmetic_sequence_example.c
	gcc -o arithmetic_sequence_example arithmetic_sequence_example.o arithmetic_sequence.o
	./arithmetic_sequence_example