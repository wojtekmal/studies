package osrodek_narciarski.dziennik;

import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.generatorMapek.GeneratorMapek;
import osrodek_narciarski.generatorMapek.pliki.WyjatekSystemuPlikow;
import osrodek_narciarski.generatorMapek.styl.GruboscKonturu;
import osrodek_narciarski.generatorMapek.styl.StylKrawedzi;
import osrodek_narciarski.generatorMapek.styl.StylLinii;
import osrodek_narciarski.generatorMapek.styl.StylWezla;
import osrodek_narciarski.osrodek.Osrodek;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Krawedz;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;
import osrodek_narciarski.sportowcy.Sportowiec;

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

    public void zrobMapke(Osrodek osrodek, String[] tekstyTras, String[] tekstyWyciagow)
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
                tekstyTras[trasa.id()]);
        }

        for (Wyciag wyciag : osrodek.wyciagi()) {
            generatorMapek.dodajKrawedz(wyciag.poczatek().id(),
                wyciag.koniec().id(),
                new StylKrawedzi(StylLinii.PRZERYWANA),
                tekstyWyciagow[wyciag.id()]);
        }
    }

    @Override
    public void dodajParametryOsrodka(Osrodek osrodek, String[] opisyTras, String[] opisyWyciagow) {
        zrobMapke();
    }

    @Override
    public void dodajStatystykiKoncowe(Osrodek osrodek, String[] statystykiTras, String[] statystykiWyciagow) {

    }

    @Override
    public void dodajPrzejazdySportowca(Osrodek osrodek, int idSportowca, Krawedz[] odwiedzoneKrawedzie) {

    }
}
