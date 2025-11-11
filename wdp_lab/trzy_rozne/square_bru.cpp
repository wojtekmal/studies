#include <iostream>
#include <vector>
#include <algorithm> // Dla std::max i std::min
#include <climits>   // Dla INT_MAX

// Używamy przestrzeni nazw std, aby uniknąć pisania std::
using namespace std;

// Struktura do przechowywania danych motelu
struct Motel {
    int network;
    int distance;
};

int main() {
    // Wyłączenie synchronizacji z C-style I/O dla szybszego wczytywania
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int n;
    cin >> n;

    // Jeśli motelów jest mniej niż 3, znalezienie trójki jest niemożliwe
    if (n < 3) {
        cout << "0 0\n";
        return 0;
    }

    // Wektor do przechowywania wszystkich moteli
    vector<Motel> motels(n);
    for (int i = 0; i < n; ++i) {
        cin >> motels[i].network >> motels[i].distance;
    }

    // Inicjalizujemy wyniki
    // min_max_dist: szukamy minimum, więc zaczynamy od nieskończoności
    // max_min_dist: szukamy maksimum, więc zaczynamy od 0 (odległości są nieujemne)
    int min_max_dist = INT_MAX;
    int max_min_dist = 0;
    bool found_triple = false;

    // ## Główna pętla O(n^2)
    // Iterujemy przez każdy motel, traktując go jako środkowy motel B
    // (indeksy od 1 do n-2, ponieważ B musi mieć A po lewej i C po prawej)
    for (int i = 1; i < n - 1; ++i) {
        Motel B = motels[i];

        // Zgodnie z poleceniem, dla każdego B iterujemy w lewo i w prawo.
        // Jest to implementacja O(n^3), która w tym zadaniu (n=1M) byłaby 
        // za wolna, ale dla mniejszych n lub dla O(n^2) musimy zoptymalizować
        // pętle wewnętrzne.
        
        // Podejście O(n^2) opisane w poleceniu:
        // "dla każdego motelu iteruje w lewo i w prawo i znajduje 
        // dwa najbliższe z obu stron... i iteruje od początku i od końca..."
        // To sugeruje znalezienie 4 kandydatów dla każdego B w czasie O(n)
        // i sprawdzenie ich w czasie O(1).

        // --- Znajdowanie kandydatów ---
        // Potrzebujemy do 2 kandydatów po lewej (A) i 2 po prawej (C),
        // aby zagwarantować, że znajdziemy trójkę różnych sieci, jeśli istnieje.

        // Bliżsi kandydaci (dla min_max_dist)
        int closest_A1_idx = -1, closest_A2_idx = -1;
        int closest_C1_idx = -1, closest_C2_idx = -1;
        
        // Dalsi kandydaci (dla max_min_dist)
        int farthest_A1_idx = -1, farthest_A2_idx = -1;
        int farthest_C1_idx = -1, farthest_C2_idx = -1;

        // --- Skanowanie w lewo (j < i) ---
        for (int j = 0; j < i; ++j) {
            if (motels[j].network == B.network) continue;

            // Najbliżsi
            if (closest_A1_idx == -1) {
                closest_A1_idx = j;
            } else if (motels[j].network != motels[closest_A1_idx].network && closest_A2_idx == -1) {
                closest_A2_idx = j;
            } else if (motels[j].network == motels[closest_A1_idx].network) {
                closest_A1_idx = j; // Aktualizuj na bliższego (większe j)
            } else if (closest_A2_idx != -1 && motels[j].network == motels[closest_A2_idx].network) {
                 closest_A2_idx = j; // Aktualizuj na bliższego (większe j)
            } else if (closest_A2_idx == -1) {
                 // Przypadek: mamy A1, ale A2 jeszcze nie ma, a sieć j jest taka sama jak A1
                 // Chcemy jednak znaleźć *drugą najbliższą sieć*, więc nadpisujemy A1
                 closest_A1_idx = j;
            }


            // Najdalsi
            if (farthest_A1_idx == -1) {
                farthest_A1_idx = j;
            } else if (motels[j].network != motels[farthest_A1_idx].network && farthest_A2_idx == -1) {
                farthest_A2_idx = j;
            }
            // Nie musimy aktualizować, bo pierwsze znalezione (najmniejsze j) są najdalsze
        }
        
        // Musimy poprawić logikę znajdowania najbliższych A
        closest_A1_idx = -1; closest_A2_idx = -1;
        for (int j = i - 1; j >= 0; --j) { // Skanuj od B w lewo
             if (motels[j].network == B.network) continue;
             if (closest_A1_idx == -1) {
                closest_A1_idx = j;
             } else if (motels[j].network != motels[closest_A1_idx].network && closest_A2_idx == -1) {
                closest_A2_idx = j;
                break; // Mamy 2 różne sieci, wystarczy
             }
        }


        // --- Skanowanie w prawo (k > i) ---
        for (int k = i + 1; k < n; ++k) {
            if (motels[k].network == B.network) continue;

            // Najbliżsi
            if (closest_C1_idx == -1) {
                closest_C1_idx = k;
            } else if (motels[k].network != motels[closest_C1_idx].network && closest_C2_idx == -1) {
                closest_C2_idx = k;
                break; // Mamy 2 różne sieci, wystarczy
            }
        }
        
        // Najdalsi
        for (int k = n - 1; k > i; --k) { // Skanuj od końca do B
            if (motels[k].network == B.network) continue;
            if (farthest_C1_idx == -1) {
                farthest_C1_idx = k;
            } else if (motels[k].network != motels[farthest_C1_idx].network && farthest_C2_idx == -1) {
                farthest_C2_idx = k;
                break; // Mamy 2 różne sieci, wystarczy
            }
        }

        // --- Zbieranie kandydatów do wektorów ---
        vector<int> left_closest_indices, right_closest_indices;
        if (closest_A1_idx != -1) left_closest_indices.push_back(closest_A1_idx);
        if (closest_A2_idx != -1) left_closest_indices.push_back(closest_A2_idx);
        if (closest_C1_idx != -1) right_closest_indices.push_back(closest_C1_idx);
        if (closest_C2_idx != -1) right_closest_indices.push_back(closest_C2_idx);
        
        vector<int> left_farthest_indices, right_farthest_indices;
        if (farthest_A1_idx != -1) left_farthest_indices.push_back(farthest_A1_idx);
        if (farthest_A2_idx != -1) left_farthest_indices.push_back(farthest_A2_idx);
        if (farthest_C1_idx != -1) right_farthest_indices.push_back(farthest_C1_idx);
        if (farthest_C2_idx != -1) right_farthest_indices.push_back(farthest_C2_idx);


        // --- Sprawdzanie par kandydatów ---

        // 1. Obliczanie min_max_dist (minimum z maksimów)
        for (int j_idx : left_closest_indices) {
            for (int k_idx : right_closest_indices) {
                // Sprawdź, czy wszystkie 3 sieci są różne
                if (motels[j_idx].network != motels[k_idx].network) {
                    found_triple = true;
                    int dist_AB = B.distance - motels[j_idx].distance;
                    int dist_BC = motels[k_idx].distance - B.distance;
                    min_max_dist = min(min_max_dist, max(dist_AB, dist_BC));
                }
            }
        }
        
        // 2. Obliczanie max_min_dist (maksimum z minimów)
         for (int j_idx : left_farthest_indices) {
            for (int k_idx : right_farthest_indices) {
                // Sprawdź, czy wszystkie 3 sieci są różne
                if (motels[j_idx].network != motels[k_idx].network) {
                    found_triple = true;
                    int dist_AB = B.distance - motels[j_idx].distance;
                    int dist_BC = motels[k_idx].distance - B.distance;
                    max_min_dist = max(max_min_dist, min(dist_AB, dist_BC));
                }
            }
        }
    } // Koniec pętli po B

    // ## Wypisanie wyników
    // Jeśli nie znaleziono żadnej trójki, obie wartości powinny wynosić 0.
    if (!found_triple) {
        cout << "0 0\n";
    } else {
        // Wypisujemy: maksimum z minimów, a potem minimum z maksimów
        cout << min_max_dist << " " << max_min_dist << "\n";
    }

    return 0;
}