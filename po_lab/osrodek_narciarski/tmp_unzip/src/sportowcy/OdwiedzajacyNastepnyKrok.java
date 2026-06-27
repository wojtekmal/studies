package sportowcy;

import czas.Moment;
import kolejkaZdarzen.zdarzenia.DolaczenieDoKolejki;
import kolejkaZdarzen.zdarzenia.RozpoczecieZjazdu;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.krawedz.OdwiedzajacyKrawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

public class OdwiedzajacyNastepnyKrok implements OdwiedzajacyKrawedz<Zdarzenie> {
    private final Sportowiec sportowiec;
    private final Moment moment;

    public OdwiedzajacyNastepnyKrok(Sportowiec sportowiec, Moment moment)
    {
        this.sportowiec = sportowiec;
        this.moment = moment;
    }

    @Override
    public Zdarzenie trasa(Trasa trasa) {
        return new RozpoczecieZjazdu(moment, trasa, sportowiec);
    }

    @Override
    public Zdarzenie wyciag(Wyciag wyciag) {
        return new DolaczenieDoKolejki(moment, wyciag, sportowiec);
    }
}
