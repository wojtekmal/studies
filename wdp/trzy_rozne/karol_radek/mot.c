#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <assert.h>
#include <stdbool.h>

typedef struct
{
    int siec, odl;
} motel;

typedef struct
{
    motel t[3];
} tab3;

const tab3 zeros = {{{0, 0}, {0, 0}, {0, 0}}};

int max(int a, int b) { return a < b ? b : a; }
int min(int a, int b) { return a < b ? a : b; }

/*
Oblicza trzy najdalsze motele różnych sieci
*/
tab3 trzy_ostatnie_rozne_motele(const motel *motele, int n, bool od_prawej)
{
    tab3 prawa = zeros;

    for (int i_prim = 0; i_prim < n; i_prim++)
    {
        // zmienia kierunek iteracji
        int i = od_prawej ? n - i_prim - 1 : i_prim;
        for (int j = 0; j < 3; j++)
        {
            if (prawa.t[j].siec == motele[i].siec)
                break;

            if (prawa.t[j].siec == 0)
            {
                prawa.t[j] = motele[i];
                break;
            }
        }
    }
    return prawa;
}

/*
Zwraca tablicę wskazującą na trzy
najbliższe na lewo motele różnych sieci
*/
void oblicz_najblizsze_motele(const motel *motele, int n, tab3 *tablica, bool od_prawej)
{
    // 3 najblizsze różne motele na prefiksie. Zera oznaczają, że nie ma takiego motelu.
    tab3 najblizsze = zeros;

    for (int i_prim = 0; i_prim < n; i_prim++)
    {
        int najdalszy_motel = INT_MAX, index_najdalszego = 0;

        // zmiana kierunku iteracji
        int i = od_prawej ? n - i_prim - 1 : i_prim;

        for (int j = 0; j < 3; j++)
        {
            if (najblizsze.t[j].siec == motele[i].siec || najblizsze.t[j].siec == 0)
            {
                najblizsze.t[j] = motele[i];
                index_najdalszego = -1;
                break;
            }
            if (od_prawej
                    ? najblizsze.t[j].odl <= najdalszy_motel
                    : najblizsze.t[j].odl >= najdalszy_motel)
            {
                najdalszy_motel = najblizsze.t[j].odl;
                index_najdalszego = j;
            }
        }

        if (index_najdalszego != -1)
            najblizsze.t[index_najdalszego] = motele[i];

        tablica[i] = najblizsze;
    }
}

bool czy_trójka_jest_poprawna(motel A, motel B, motel C)
{
    if (A.siec == 0 || C.siec == 0 || A.odl > B.odl || B.odl > C.odl)
        return false;
    return A.siec != C.siec &&
           C.siec != B.siec &&
           A.siec != B.siec;
}

void dopasuj(tab3 L, motel B, tab3 R, int *wyn_min_max, int *wyn_max_min)
{
    for (int j = 0; j < 3; j++)
    {
        for (int k = 0; k < 3; k++)
        {
            motel A = L.t[j],
                  C = R.t[k];

            if (czy_trójka_jest_poprawna(A, B, C))
            {
                int odl_BC = C.odl - B.odl,
                    odl_AB = B.odl - A.odl;
                *wyn_min_max = min(*wyn_min_max, max(odl_BC, odl_AB));
                *wyn_max_min = max(*wyn_max_min, min(odl_BC, odl_AB));
            }
        }
    }
}

void solve(const motel *motele, int n, int *wyn_max_min, int *wyn_min_max)
{
    tab3 *najblizsze_po_lewej = (tab3 *)malloc((unsigned)n * sizeof(tab3));
    tab3 *najblizsze_po_prawej = (tab3 *)malloc((unsigned)n * sizeof(tab3));

    oblicz_najblizsze_motele(motele, n, najblizsze_po_lewej, false);
    oblicz_najblizsze_motele(motele, n, najblizsze_po_prawej, true);

    tab3 pierwsze_rozne = trzy_ostatnie_rozne_motele(motele, n, false);
    tab3 ostatnie_rozne = trzy_ostatnie_rozne_motele(motele, n, true);

    /*
    Dopasowuje do motel[i] najbliższe pasujące po lewej i po prawej stronie
    Oraz najdalsze pasujące po prawej i lewej stronie
    */

    for (int i = 1; i < n - 1; i++)
    {
        dopasuj(pierwsze_rozne, motele[i], ostatnie_rozne, wyn_min_max, wyn_max_min);
        dopasuj(najblizsze_po_lewej[i - 1], motele[i], najblizsze_po_prawej[i + 1], wyn_min_max, wyn_max_min);
    }
}

int main()
{
    int n;
    assert(scanf("%i", &n) == 1);

    motel *motele = (motel *)malloc((unsigned)n * sizeof(motel));

    for (int i = 0; i < n; i++)
    {
        int siec, odl;
        assert(scanf("%i %i", &siec, &odl) == 2);
        motele[i] = (motel){siec, odl};
    }

    int wyn_max_min = 0,
        wyn_min_max = INT_MAX;

    solve(motele, n, &wyn_max_min, &wyn_min_max);

    if (wyn_min_max == INT_MAX)
        wyn_min_max = 0;

    printf("%i %i\n", wyn_min_max, wyn_max_min);
}