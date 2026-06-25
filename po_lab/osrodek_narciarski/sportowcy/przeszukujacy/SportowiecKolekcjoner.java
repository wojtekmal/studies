package osrodek_narciarski.sportowcy.przeszukujacy;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.losowosc.MaszynaLosujaca;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.sportowcy.Sportowiec;

import java.util.HashMap;
import java.util.Map;

public class SportowiecKolekcjoner extends SportowiecPrzeszukujacy{

    private Map<Trasa, Integer> licznikiPrzejazdow = new HashMap<>();

    public SportowiecKolekcjoner(SportowiecKolekcjoner inny,
        int przesuniecieId,
        Interwal przesuniecieMomentuStartu)
    {
        super(inny, przesuniecieId, przesuniecieMomentuStartu);
    }

    @Override
    public boolean czyNowaTrasaLepsza(Trasa najlepszaTrasa, Trasa nowa) {
        return licznikiPrzejazdow.get(nowa) < licznikiPrzejazdow.get(najlepszaTrasa);
    }

    @Override
    public Sportowiec kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return null;
    }

    @Override
    public void zanotujPrzejazdTrasa(Trasa trasa)
    {
        zanotujOstatnieOdwiedziny(trasa);

        int liczbaPrzejazdow = licznikiPrzejazdow.getOrDefault(trasa, 0);
        licznikiPrzejazdow.put(trasa, liczbaPrzejazdow + 1);
    }
}
