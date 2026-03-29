#!/bin/bash

TIMEFORMAT=%R

g++ -o test_generator.exe test_generator.cpp
echo 25 | ./test_generator > test.in

echo "Wszystkie wyniki podawane w sekundach."

echo -n "C++ (optymalizacja -O3): "
g++ -o largest_clique_size.exe largest_clique_size.cpp -std=c++20 -O3 -Wall
time ./largest_clique_size.exe < test.in > /dev/null

echo -n "Java (-Xcomp - wymuszona kompilacja całości): "
javac LargestCliqueSize.java
time java -Xcomp LargestCliqueSize < test.in > /dev/null

echo -n "Java (domyślny / -Xmixed): "
javac LargestCliqueSize.java
time java -Xmixed LargestCliqueSize < test.in > /dev/null

echo -n "Java (-Xint - tryb interpretowany): "
javac LargestCliqueSize.java
time java -Xint LargestCliqueSize < test.in > /dev/null