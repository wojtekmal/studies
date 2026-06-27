package dziennik;

import czas.Moment;
import sportowcy.Sportowiec;

public class DziennikStandardoweWyjscie implements Dziennik {

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
    public void dodajStatystykiKoncowe(String[] statystykiTras, String[] statystykiWyciagow) {
        for (String statystykiTrasy : statystykiTras) {
            System.out.println(statystykiTrasy);
        }

        for (String statystykiWyciagu : statystykiWyciagow) {
            System.out.println(statystykiWyciagu);
        }
    }
}
