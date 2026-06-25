package osrodek_narciarski.kolejkaZdarzen;

import osrodek_narciarski.kolejkaZdarzen.zdarzenia.Zdarzenie;

import java.util.PriorityQueue;

public class KolejkaZdarzenStandardowa implements KolejkaZdarzen {
    private class ZdarzenieZIndeksem {
        private Zdarzenie zdarzenie;
        private int indeks;

        public ZdarzenieZIndeksem(Zdarzenie zdarzenie, int indeks)
        {
            this.zdarzenie = zdarzenie;
            this.indeks = indeks;
        }
    }
    PriorityQueue<Zdarzenie> kolejkaZdarzen = new PriorityQueue<>();

    @Override
    public void dodaj(Zdarzenie zdarzenie) {

    }

    @Override
    public Zdarzenie zdejmij() {
        return null;
    }

    @Override
    public boolean czyPusta() {
        return false;
    }
}
