package osrodek_narciarski.sportowcy;

import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek_narciarski.osrodek.krawedz.OdwiedzajacyKrawedz;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;

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
        return sportowiec.nastepnyKrokTrasa(moment, trasa);
    }

    @Override
    public Zdarzenie wyciag(Wyciag wyciag) {
        return sportowiec.nastepnyKrokWyciag(moment, wyciag);
    }
}
