package sportowcy.przeszukujacy;

import osrodek.krawedz.OdwiedzajacyKrawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

public class OdwiedzajacyNajlepszaKrawedz implements OdwiedzajacyKrawedz<Void> {
    private final SportowiecPrzeszukujacy sportowiec;
    private Trasa najlepszaTrasa = null;

    public OdwiedzajacyNajlepszaKrawedz(SportowiecPrzeszukujacy sportowiec) {
        this.sportowiec = sportowiec;
    }

    @Override
    public Void trasa(Trasa trasa) {
        if (najlepszaTrasa == null || sportowiec.czyNowaTrasaLepsza(najlepszaTrasa, trasa)) {
            najlepszaTrasa = trasa;
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
