#include <bits/stdc++.h>
using namespace std;

void wygeneruj_male_testy(mt19937 rng)
{
    const int LICZBA_MALYCH_TESTOW = 10000;
    string male_testy_katalog = "dyskretne-fraktale-testy/testy_poprawnosci_male";

    for (int i = 0; i < LICZBA_MALYCH_TESTOW; i++)
    {
        uint32_t n = rng();

        
    }
}

int main()
{
    mt19937 rng{};

    wygeneruj_male_testy(rng);
}