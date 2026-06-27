package symulacja;

import czas.Moment;
import dziennik.Dziennik;
import dziennik.TworcaMapek;
import generatorMapek.pliki.WyjatekSystemuPlikow;
import kolejkaZdarzen.KolejkaZdarzen;
import kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import kolejkaZdarzen.zdarzenia.PoczatekDnia;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Osrodek;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.Sportowiec;

public class Symulacja {

    public static final Moment POCZATEK_DNIA = new Moment(9, 0, 0);

    public static final Moment KONIEC_SYMULACJI = new Moment(15, 0, 0);

    public void przeprowadzSymulacje(Dziennik dziennik,
        TworcaMapek tworcaMapek,
        KolejkaZdarzen kolejkaZdarzen,
        Osrodek osrodek,
        Sportowiec[] sportowcy) throws WyjatekSystemuPlikow {
        przygotujPoczatkoweZdarzenia(kolejkaZdarzen, osrodek, sportowcy);
        glownaPetla(kolejkaZdarzen, dziennik);
        zbierzStatystyki(osrodek, dziennik);
        zrobMapki(osrodek, sportowcy, tworcaMapek);
    }

    /**
     * Inicjuje kolejkę zdarzeń poprzez wrzucenie na nią początkowych zdarzeń:
     * 1. Początek dnia na stoku dla każdego sportowca zgodnie z jego momentem startu.
     * 2. Pierwszy odjazd wagonika dla każdego wyciągu dokładnie o 9:00:00.
     */
    private void przygotujPoczatkoweZdarzenia(KolejkaZdarzen kolejkaZdarzen, Osrodek osrodek, Sportowiec[] sportowcy) {
        for (Sportowiec sportowiec : sportowcy) {
            kolejkaZdarzen.dodaj(new PoczatekDnia(sportowiec.momentStartu(), sportowiec.wezelStartowy(), sportowiec));
        }
        for (Wyciag wyciag : osrodek.wyciagi()) {
            kolejkaZdarzen.dodaj(new OdjazdWyciagu(POCZATEK_DNIA, wyciag));
        }
    }

    /**
     * Dopóki kolejka zdarzeń nie jest pusta, kolejno:
     * 1. Zdejmujemy następne zdarzenie z kolejki,
     * 2. Przetwarzamy je,
     * 3. Dorzucamy na kolejkę nowe zdarzenia, o ile chcemy je jeszcze przetwarzać.
     */
    private void glownaPetla(KolejkaZdarzen kolejkaZdarzen, Dziennik dziennik) {
        while (!kolejkaZdarzen.czyPusta()) {
            Zdarzenie nastepneZdarzenie = kolejkaZdarzen.zdejmij();
            Zdarzenie[] noweZdarzenia = nastepneZdarzenie.przetworz(dziennik);

            for (Zdarzenie noweZdarzenie : noweZdarzenia) {
                if (noweZdarzenie.moment().wczesniejNiz(KONIEC_SYMULACJI)
                    || noweZdarzenie.czyPrzetwarzacPoZakonczeniuSymulacji()) {
                    kolejkaZdarzen.dodaj(noweZdarzenie);
                }
            }
        }
    }

    /**
     * Wypisuje statystyki końcowe symulacji.
     */
    private void zbierzStatystyki(Osrodek osrodek, Dziennik dziennik) {
        String[] statystykiTras = new String[osrodek.trasy().length];
        String[] statystykiWyciagow = new String[osrodek.wyciagi().length];

        for (int i = 0; i < osrodek.trasy().length; i++) {
            statystykiTras[i] = osrodek.trasy()[i].wypiszStatystyki();
        }
        for (int i = 0; i < osrodek.wyciagi().length; i++) {
            statystykiWyciagow[i] = osrodek.wyciagi()[i].wypiszStatystyki();
        }

        dziennik.dodajStatystykiKoncowe(statystykiTras, statystykiWyciagow);
    }

    private void zrobMapki(Osrodek osrodek, Sportowiec[] sportowcy, TworcaMapek tworcaMapek) throws WyjatekSystemuPlikow {
        String[] parametryTras = new String[osrodek.trasy().length];
        String[] parametryWyciagow = new String[osrodek.wyciagi().length];
        String[] statystykiTras = new String[osrodek.trasy().length];
        String[] statystykiWyciagow = new String[osrodek.wyciagi().length];

        for (Trasa trasa : osrodek.trasy()) {
            parametryTras[trasa.id()] = trasa.wypiszParametry();
            statystykiTras[trasa.id()] = trasa.wypiszStatystyki();
        }

        for (Wyciag wyciag : osrodek.wyciagi()) {
            parametryWyciagow[wyciag.id()] = wyciag.wypiszParametry();
            statystykiWyciagow[wyciag.id()] = wyciag.wypiszStatystyki();
        }

        tworcaMapek.dodajParametryOsrodka(osrodek, parametryTras, parametryWyciagow);
        tworcaMapek.dodajStatystykiKoncowe(osrodek, statystykiTras, statystykiWyciagow);

        int idSportowca = 0;

        for (Sportowiec sportowiec : sportowcy) {
            if (sportowiec.sledzony()) {
                tworcaMapek.dodajPrzejazdySportowca(osrodek,
                    idSportowca,
                    sportowiec.odwiedzoneKrawedzie());
            }

            idSportowca++;
        }
    }
}
