#!/bin/bash

TIMEFORMAT=%R

g++ -o test_generator.exe test_generator.cpp

echo "Nazwa porównywanego języka: C++."
echo -n "Środowisko: "
uname -mrs
echo -n "Wersja Javy: "
javac --version
echo -n "Wersja porównywanego języka: "
c++ --version | head -n 1
echo "Zadanie: znaleźć rozmiar największej kliki."
echo


echo "Wszystkie wyniki podawane w sekundach, z dokładnością do milisekundy."
echo "25 wierchołków:"
echo 25 | ./test_generator.exe > test25.in

echo -n "C++ (optymalizacja -O3): "
g++ -o largest_clique_size.exe largest_clique_size.cpp -std=c++20 -O3 -Wall
time ./largest_clique_size.exe < test25.in > /dev/null

echo -n "Java (-Xcomp - wymuszona kompilacja całości): "
javac LargestCliqueSize.java
time java -Xcomp LargestCliqueSize < test25.in > /dev/null

echo -n "Java (domyślny / -Xmixed): "
javac LargestCliqueSize.java
time java -Xmixed LargestCliqueSize < test25.in > /dev/null

echo -n "Java (-Xint - tryb interpretowany): "
javac LargestCliqueSize.java
time java -Xint LargestCliqueSize < test25.in > /dev/null

echo
echo "30 wierzchołków:"
echo 30 | ./test_generator.exe > test30.in

echo -n "C++ (optymalizacja -O3): "
g++ -o largest_clique_size.exe largest_clique_size.cpp -std=c++20 -O3 -Wall
time ./largest_clique_size.exe < test30.in > /dev/null

echo -n "Java (-Xcomp - wymuszona kompilacja całości): "
javac LargestCliqueSize.java
time java -Xcomp LargestCliqueSize < test30.in > /dev/null

echo -n "Java (domyślny / -Xmixed): "
javac LargestCliqueSize.java
time java -Xmixed LargestCliqueSize < test30.in > /dev/null

echo -n "Java (-Xint - tryb interpretowany): "
javac LargestCliqueSize.java
time java -Xint LargestCliqueSize < test30.in > /dev/null