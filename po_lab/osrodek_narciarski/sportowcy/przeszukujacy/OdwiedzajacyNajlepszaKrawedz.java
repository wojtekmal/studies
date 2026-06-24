package osrodek_narciarski.sportowcy.przeszukujacy;

import osrodek_narciarski.osrodek.krawedz.OdwiedzajacyKrawedz;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.osrodek.krawedz.wyciag.Wyciag;

public class OdwiedzajacyNajlepszaKrawedz implements OdwiedzajacyKrawedz<Void> {
    private final SportowiecPrzeszukujacy sportowiec;
    private Trasa najlepszaTrasa = null;

    public OdwiedzajacyNajlepszaKrawedz(SportowiecPrzeszukujacy sportowiec) {
        this.sportowiec = sportowiec;
    }

    @Override
    public Void trasa(Trasa trasa) {
        if (najlepszaTrasa == null) {
            najlepszaTrasa = trasa;
        }
        else {
            najlepszaTrasa = sportowiec.lepszaTrasa(najlepszaTrasa, trasa);
        }
        return null;
    }

    @Override
    public Void wyciag(Wyciag wyciag) {
        return null;
    }

    public Trasa najlepszaTrasa()
    {
        return najlepszaTrasa;
    }
}
