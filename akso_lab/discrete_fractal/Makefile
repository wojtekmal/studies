discrete_fractal_c:
	gcc -o discrete_fractal_c discrete_fractal.c -std=gnu23 -Wall -O3

discrete_fractal:
	nasm -f elf64 -w+all -w+error -w-unknown-warning -w-reloc-rel -o discrete_fractal.o discrete_fractal.asm -g
	ld -pie -I /lib64/ld-linux-x86-64.so.2 --fatal-warnings -o discrete_fractal discrete_fractal.o -g