package osrodek_narciarski.kolejkaZdarzen.zdarzenia;

import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.dziennik.Dziennik;
import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;
import osrodek_narciarski.sportowcy.Sportowiec;

public class DolaczenieDoKolejki extends Zdarzenie {

    private final Wyciag wyciag;

    private final Sportowiec sportowiec;

    public DolaczenieDoKolejki(Moment moment, Wyciag wyciag, Sportowiec sportowiec) {
        super(moment);
        this.wyciag = wyciag;
        this.sportowiec = sportowiec;
    }

    public Wyciag wyciag() {
        return wyciag;
    }

    public Sportowiec sportowiec() {
        return sportowiec;
    }

    @Override
    public Zdarzenie[] przetworz(Dziennik dziennik) {
        dziennik
            .dodajWpisZeSportowcem(moment, sportowiec, String.format("dołączył do kolejki w %s", wyciag.toString()));

        wyciag.dodajDoKolejki(sportowiec);

        return new Zdarzenie[0];
    }

    @Override
    public boolean czyPrzetwarzacPoZakonczeniuSymulacji() {
        return false;
    }

    @Override
    public String toString() {
        return "DolaczenieDoKolejki [wyciag=" + wyciag + ", sportowiec=" + sportowiec + ", super=" + super.toString()
            + "]";
    }
}
