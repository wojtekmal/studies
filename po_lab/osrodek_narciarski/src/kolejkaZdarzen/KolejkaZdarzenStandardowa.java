package kolejkaZdarzen;

import kolejkaZdarzen.zdarzenia.Zdarzenie;

import java.util.PriorityQueue;

public class KolejkaZdarzenStandardowa implements KolejkaZdarzen {

    private class ZdarzenieZId {
        public Zdarzenie zdarzenie;
        public int id;

        public ZdarzenieZId(Zdarzenie zdarzenie, int id)
        {
            this.zdarzenie = zdarzenie;
            this.id = id;
        }
    }

    private int nastepneIdZdarzenia = 0;

    PriorityQueue<ZdarzenieZId> kolejkaZdarzen = new PriorityQueue<>((zdarzenieZId1, zdarzenieZId2) -> {
        int porownanieMomentow = zdarzenieZId1.zdarzenie.moment().compareTo(zdarzenieZId2.zdarzenie.moment());

        if (porownanieMomentow != 0) {
            return porownanieMomentow;
        }
        else {
            return Integer.compare(zdarzenieZId1.id, zdarzenieZId2.id);
        }
    });

    @Override
    public void dodaj(Zdarzenie zdarzenie) {
        int id = nastepneIdZdarzenia;
        nastepneIdZdarzenia++;

        kolejkaZdarzen.add(new ZdarzenieZId(zdarzenie, id));
    }

    @Override
    public Zdarzenie zdejmij() {
        return kolejkaZdarzen.remove().zdarzenie;
    }

    @Override
    public boolean czyPusta() {
        return kolejkaZdarzen.isEmpty();
    }
}
