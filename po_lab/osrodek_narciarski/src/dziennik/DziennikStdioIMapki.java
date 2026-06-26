package dziennik;

import czas.Moment;
import generatorMapek.GeneratorMapek;
import generatorMapek.pliki.WyjatekSystemuPlikow;
import generatorMapek.styl.GruboscKonturu;
import generatorMapek.styl.StylKrawedzi;
import generatorMapek.styl.StylLinii;
import generatorMapek.styl.StylWezla;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.Sportowiec;

import java.util.*;

public class DziennikStdioIMapki implements Dziennik {

    private final GeneratorMapek generatorMapek = new GeneratorMapek("mapki");

    public DziennikStdioIMapki() throws WyjatekSystemuPlikow
    {
    }

    @Override
    public void dodajWpis(Moment moment, String wpis) {
        System.out.printf("%s: %s%n", moment.toString(), wpis);
    }

    @Override
    public void dodajWpisZeSportowcem(Moment moment, Sportowiec sportowiec, String coZrobil) {
        if (sportowiec.sledzony()) {
            dodajWpis(moment, String.format("%s %s.", sportowiec, coZrobil));
        }
    }

    @Override
    public void dodajTabele(String[][] wiersze) {
        int maxDlugoscKlucza = 0;

        for (String[] wiersz : wiersze) {
            maxDlugoscKlucza = Math.max(maxDlugoscKlucza, wiersz[0].length());
        }

        String formatWiersza = "%-" + maxDlugoscKlucza + "s: %s%n";

        for (String[] wiersz : wiersze) {
            System.out.format(formatWiersza, wiersz[0], wiersz[1]);
        }
    }

    public void zrobMapke(Osrodek osrodek, String[] tekstyTras, String[] tekstyWyciagow, String nazwaPliku) throws WyjatekSystemuPlikow
    {
        for (Wezel wezel : osrodek.wezly()) {
            GruboscKonturu gruboscKonturu = wezel.czyStartowy() ? GruboscKonturu.POGRUBIONY : GruboscKonturu.ZWYKLY;

            generatorMapek.dodajWezel(wezel.id(),
                wezel.wspolrzednaX(),
                wezel.wspolrzednaY(),
                new StylWezla(gruboscKonturu));
        }

        for (Trasa trasa : osrodek.trasy()) {
            generatorMapek.dodajKrawedz(trasa.poczatek().id(),
                trasa.koniec().id(),
                new StylKrawedzi(StylLinii.CIAGLA),
                podzielNaLinie(tekstyTras[trasa.id()]));
        }

        for (Wyciag wyciag : osrodek.wyciagi()) {
            generatorMapek.dodajKrawedz(wyciag.poczatek().id(),
                wyciag.koniec().id(),
                new StylKrawedzi(StylLinii.PRZERYWANA),
                podzielNaLinie(tekstyWyciagow[wyciag.id()]));
        }

        generatorMapek.tworzMapke(nazwaPliku);
        generatorMapek.zeruj();
    }

    private List<String> podzielNaLinie(String doPodzialu) {
        return Arrays.asList(doPodzialu.split("\n"));
    }

    @Override
    public void dodajParametryOsrodka(Osrodek osrodek, String[] opisyTras, String[] opisyWyciagow) throws WyjatekSystemuPlikow
    {
        zrobMapke(osrodek, opisyTras, opisyWyciagow, "parametry_osrodka.tex");
    }

    @Override
    public void dodajStatystykiKoncowe(Osrodek osrodek, String[] statystykiTras, String[] statystykiWyciagow) throws WyjatekSystemuPlikow
    {
        zrobMapke(osrodek, statystykiTras, statystykiWyciagow, "statystyki_koncowe.tex");
    }

    @Override
    public void dodajPrzejazdySportowca(Osrodek osrodek, int idSportowca, Krawedz[] odwiedzoneKrawedzie) throws WyjatekSystemuPlikow
    {
        Map<Krawedz, Set<Integer>> zbioryOdwiedzin = new HashMap<>();
        int nastepnyIndeksOdwiedzonego = 0;

        for (Krawedz krawedz : odwiedzoneKrawedzie) {
            Set<Integer> zbiorOdwiedzin = zbioryOdwiedzin.computeIfAbsent(krawedz,
                    k -> new HashSet<>());

            zbiorOdwiedzin.add(nastepnyIndeksOdwiedzonego);
            nastepnyIndeksOdwiedzonego++;
        }

        String[] opisyTras = new String[osrodek.trasy().length];
        String[] opisyWyciagow = new String[osrodek.wyciagi().length];

        for (Trasa trasa : osrodek.trasy()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("t%d(%d): ",
                trasa.id(), zbioryOdwiedzin.get(trasa).size()));

            String opis = dodajOpisZbioruOdwiedzin(stringBuilder,
                zbioryOdwiedzin.computeIfAbsent(trasa, k -> new HashSet<>()));

            opisyTras[trasa.id()] = opis;
        }

        for (Wyciag wyciag : osrodek.wyciagi()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("w%d(%d): ",
                wyciag.id(), zbioryOdwiedzin.get(wyciag).size()));

            String opis = dodajOpisZbioruOdwiedzin(stringBuilder,
                zbioryOdwiedzin.computeIfAbsent(wyciag, k -> new HashSet<>()));

            opisyWyciagow[wyciag.id()] = opis;
        }

        String nazwaPliku = String.format("przejazd_sportowca_%d.tex", idSportowca);
        zrobMapke(osrodek, opisyTras, opisyWyciagow, nazwaPliku);
    }

    private String dodajOpisZbioruOdwiedzin(StringBuilder stringBuilder, Set<Integer> zbiorOdwiedzin) {
        for (int idOdwiedzonejKrawedzi : zbiorOdwiedzin) {
            stringBuilder.append(idOdwiedzonejKrawedzi).append(",");
        }

        return stringBuilder.substring(0, stringBuilder.length());
    }


}
