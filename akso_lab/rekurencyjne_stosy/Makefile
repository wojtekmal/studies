.PHONY: all clean

rstack.o: rstack.c rstack.h
	gcc -c -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC -O2 rstack.c

memory_tests.o: memory_tests.c memory_tests.h
	gcc -c -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC -O2 \
	memory_tests.c

librstack.so: rstack.o memory_tests.o
	gcc -o librstack.so -shared -Wl,--wrap=malloc -Wl,--wrap=calloc \
	-Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free \
	-Wl,--wrap=strdup -Wl,--wrap=strndup memory_tests.o rstack.o

rstack_example: librstack.so rstack_example.c
	gcc -o rstack_example rstack_example.c -std=gnu23 -L. -lrstack \
	-Wl,-rpath='$$ORIGIN'

#rstack_g.o: rstack.c rstack.h
#	gcc -c -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC rstack.c \
#	-g -o rstack_g.o
#
#memory_tests_g.o: memory_tests.c memory_tests.h
#	gcc -c -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC \
#	memory_tests.c -g -o memory_tests_g.o
#
#librstack_g.so: rstack_g.o memory_tests_g.o
#	gcc -o librstack.so -shared -Wl,--wrap=malloc -Wl,--wrap=calloc \
#	-Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free \
#	-Wl,--wrap=strdup -Wl,--wrap=strndup memory_tests_g.o rstack_g.o -g
#
#rstack_example_g: librstack_g.so rstack_example.c
#	gcc -o rstack_example_g rstack_example.c -std=gnu23 -L. -lrstack \
#	-Wl,-rpath='$$ORIGIN' -g