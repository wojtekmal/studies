.PHONY: all clean build_zip

rstack.o: rstack.c rstack.h
	gcc -c -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC -O2 rstack.c -g

memory_tests.o: memory_tests.c memory_tests.h
	gcc -c -Wall -Wextra -Wno-implicit-fallthrough -std=gnu23 -fPIC -O2 \
	memory_tests.c -g

librstack.so: rstack.o memory_tests.o
	gcc -o librstack.so -shared -Wl,--wrap=malloc -Wl,--wrap=calloc \
	-Wl,--wrap=realloc -Wl,--wrap=reallocarray -Wl,--wrap=free \
	-Wl,--wrap=strdup -Wl,--wrap=strndup memory_tests.o rstack.o -g

rstack_example: librstack.so rstack_example.c
	gcc -o rstack_example rstack_example.c -std=gnu23 -L. -lrstack \
	-Wl,-rpath='$$ORIGIN'

clean:
	rm *.o

build_zip:
	zip rekurencyjne_stosy_wojciech_malinowski.zip -j rstack.c Makefile

run_tests: librstack.so
	gcc -o rstack_tests rstack_tests.c -std=gnu23 -L. -lrstack \
	-Wl,-rpath='$$ORIGIN' -g
	./rstack_tests memory_read