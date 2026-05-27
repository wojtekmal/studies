discrete_fractal_c:
	gcc -o discrete_fractal_c discrete_fractal.c -std=gnu23 -Wall -O3

test_discrete_fractal_c: discrete_fractal_c
	cd dyskretne-fraktale-testy
	bash testy_blednego_wejscia.sh ../discrete_fractal_c
	cd ..

discrete_fractal:
	nasm -f elf64 -w+all -w+error -w-unknown-warning -w-reloc-rel -o discrete_fractal.o discrete_fractal.asm
	ld -pie -I /lib64/ld-linux-x86-64.so.2 --fatal-warnings -o discrete_fractal discrete_fractal.o

test_discrete_fractal: discrete_fractal
	cd dyskretne-fraktale-testy
	bash testy_blednego_wejscia.sh ../discrete_fractal
	cd ..