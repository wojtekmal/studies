package połączenie;

import sportowiec.Sportowiec;
import struktury_danych.KolejkaNaWyciąg;
import struktury_danych.KolejkaZdarzeń;
import węzeł.Węzeł;
import zdarzenie.Godzina;
import zdarzenie.OdjazdKrzesełka;
import zdarzenie.OdjazdWyciągiem;
import zdarzenie.PrzyjazdWyciągiem;
import zdarzenie.WejścieDoKolejki;

public class Wyciąg extends Połączenie
{
    private int odstępCzasowy;
    private int rozmiarKrzesła;
    private int czasPrzejazdu;
    private KolejkaZdarzeń kolejkaZdarzeń;
    private KolejkaNaWyciąg kolejkaNaWyciąg;

    public void dodajKrzesełko(Godzina godzinaOdjazdu)
    {
        // Zakładamy, że wyciągi zaczynają pracę wtedy, gdy się zaczyna
        // symulacja, czyli o 9:00:00. Każdy wyciąg wrzuca na kolejkę zdarzeń
        // wszystkie odjazdy krzesełek. Wyciągi są wczytywane przed sportowcami,
        // czyli odjazdy krzesełek będą wcześniej wrzucone na kolejkę niż
        // wejścia sportowców na pierwsze wyciągi, czyli wydarzą się wcześniej,
        // nawet jeśli sportowiec przyjdzie równo o 9:00:00, czyli pierwsze
        // krzesełko każdego wyciągu będzie puste.

        OdjazdKrzesełka zdarzenie = new OdjazdKrzesełka(godzinaOdjazdu, this);

        kolejkaZdarzeń.dodajZdarzenie(zdarzenie);
    }

    public void zaplanujKolejneKrzesełko(Godzina godzinaPoprzedniego)
    {
        Godzina godzinaOdjazdu = godzinaPoprzedniego.dodaj(odstępCzasowy);

        dodajKrzesełko(godzinaOdjazdu);
    }

    public Wyciąg(Węzeł początkowy, Węzeł końcowy, int odstępCzasowy,
        int rozmiarKrzesła, int czasPrzejazdu, KolejkaZdarzeń kolejkaZdarzeń,
        KolejkaNaWyciąg kolejkaNaWyciąg, int id)
    {
        super(początkowy, końcowy, kolejkaZdarzeń, id);
        
        this.odstępCzasowy = odstępCzasowy;
        this.rozmiarKrzesła = rozmiarKrzesła;
        this.czasPrzejazdu = czasPrzejazdu;
        this.kolejkaZdarzeń = kolejkaZdarzeń;
        this.kolejkaNaWyciąg = kolejkaNaWyciąg;

        dodajKrzesełko(new Godzina("09:00:00"));

        początkowy.dodajWyciąg(this);
    }

    public void przyjmijZKolejki(Godzina godzina)
    {
        Godzina godzinaPrzyjazdu = godzina.dodaj(czasPrzejazdu);

        for (int i = 0; i < rozmiarKrzesła && !kolejkaNaWyciąg.jestPusta(); i++)
        {
            Sportowiec sportowiec = kolejkaNaWyciąg.dajKolejnego();

            OdjazdWyciągiem odjazd = new OdjazdWyciągiem(godzina, sportowiec,
                this);
            kolejkaZdarzeń.dodajZdarzenie(odjazd);

            PrzyjazdWyciągiem przyjazd = new PrzyjazdWyciągiem(
                godzinaPrzyjazdu, sportowiec, this);
            kolejkaZdarzeń.dodajZdarzenie(przyjazd);

            liczbaPrzejazdów++;
        }
    }

    public void wybierzPołączenie(Godzina godzina, Sportowiec sportowiec)
    {
        WejścieDoKolejki zdarzenie = new WejścieDoKolejki(godzina, sportowiec,
            this);
        kolejkaZdarzeń.dodajZdarzenie(zdarzenie);
    }

    public void dodajDoKolejki(Sportowiec sportowiec)
    {
        kolejkaNaWyciąg.dodaj(sportowiec);
    }

    public void wypiszStatystyki()
    {
        String format = "Łączna liczba przejazdów wyciągiem %d: %d.";
        System.out.println(String.format(format, id, liczbaPrzejazdów));
    }
}
