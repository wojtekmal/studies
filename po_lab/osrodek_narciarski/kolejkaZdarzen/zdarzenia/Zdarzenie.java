package osrodek_narciarski.kolejkaZdarzen.zdarzenia;

import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.dziennik.Dziennik;

public abstract class Zdarzenie {

    protected final Moment moment;

    public Zdarzenie(Moment moment) {
        this.moment = moment;
    }

    public Moment moment() {
        return moment;
    }

    /**
     * Wywołuje odpowiednie efekty zdarzenia i daje nowo stworzone zdarzenia
     * w kolejności niemalejących momentów.
     */
    public abstract Zdarzenie[] przetworz(Dziennik dziennik);

    public abstract boolean czyPrzetwarzacPoZakonczeniuSymulacji();

    @Override
    public String toString() {
        return "Zdarzenie [moment=" + moment + "]";
    }
}
