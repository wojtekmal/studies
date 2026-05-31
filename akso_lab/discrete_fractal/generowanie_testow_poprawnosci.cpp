#include <bits/stdc++.h>
using namespace std;

void wygeneruj_male_testy(mt19937 rng)
{
    const int LICZBA_MALYCH_TESTOW = 10000;
    string male_testy_katalog = "dyskretne-fraktale-testy/testy_poprawnosci_male";

    for (int i = 0; i < LICZBA_MALYCH_TESTOW; i++)
    {
        // n od 0 do 4.
        uint32_t n = rng() % 5;

        string nazwa_pliku_wejscia = "id_" + to_string(i) + "_n_" + to_string(n) + ".in";
        string sciezka_do_pliku_wejscia = male_testy_katalog + "/" + nazwa_pliku_wejscia;
        ofstream plik(sciezka_do_pliku_wejscia);

        // Losujemy liczbę znaków (od 1 do 5) oraz znaki (może wyjść mniej jeśli
        // się przypadkiem powtórzą).
        int liczba_znakow_powtarzane = rng() % 5 + 1;

        set<char> znaki_set;
        for (int j = 0; j < liczba_znakow_powtarzane; j++)
        {
            znaki_set.insert(rng() % ('~' - '!' + 1) + '!');
        }

        // Losujemy długość początkowego napisu, od 0 do 4.
        int dlugosc_poczatkowego_napisu = rng() % 5;

        vector<char> znaki(znaki_set.begin(), znaki_set.end());
        int liczba_znakow = znaki.size();

        for (int j = 0; j < dlugosc_poczatkowego_napisu; j++)
        {
            plik << znaki[rng() % liczba_znakow];
        }
        plik << "\n";

        // Dla każdego znaku losujemy, czy go podmieniamy.
        for (int j = 0; j < liczba_znakow; j++)
        {
            if (rng() % 2) continue;

            plik << znaki[j];

            // Losujemy liczbę znaków w zasadzie podmieniania - od 0 do 4.
            int liczba_znaków_w_zasadzie = rng() % 5;

            for (int k = 0; k < liczba_znaków_w_zasadzie; k++)
            {
                plik << znaki[rng() % liczba_znakow];
            }

            plik << "\n";
        }

        plik.close();

        string nazwa_pliku_wyjscia = "id_" + to_string(i) + ".out";
        string sciezka_do_pliku_wyjscia = male_testy_katalog + "/" + nazwa_pliku_wyjscia;
        string komenda = "./discrete_fractal " + to_string(n) + " < " + 
            sciezka_do_pliku_wejscia + " > " + sciezka_do_pliku_wyjscia;
        
        system(komenda.c_str());
    }
}

int main()
{
    mt19937 rng{};

    wygeneruj_male_testy(rng);
}