.PHONY: all clean

rstack.o: rstack.c rstack.h
	gcc -c -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC -O2 rstack.c

memory_tests.o: memory_tests.c memory_tests.h
	gcc -c -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC -O2 rstack.c

librstack.so: rstack.o memory_tests.o
	gcc -o librstack.so -shared -Wl,--wrap=malloc -Wl,--wrap=calloc -Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free -Wl,--wrap=strdup -Wl,--wrap=strndup

rstack_example: librstack.so rstack_example.c
	gcc -o rstack_example -lrstack rstack_example.c