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

    public SportowiecKolekcjoner(int id,
        int poziomZaawansowania,
        double wspolczynnikSpontanicznosci,
        double wspolczynnikZnudzenia,
        double wagaTrudnosci,
        double wagaNawierzchni,
        double wagaZnudzenia,
        boolean sledzony,
        Wezel wezelStartowy,
        Moment momentStartu,
        MaszynaLosujaca maszynaLosujaca) {
        super(id,
            poziomZaawansowania,
            wspolczynnikSpontanicznosci,
            wspolczynnikZnudzenia,
            wagaTrudnosci,
            wagaNawierzchni,
            wagaZnudzenia,
            sledzony,
            wezelStartowy,
            momentStartu,
            maszynaLosujaca);
    }

    public SportowiecKolekcjoner(SportowiecKolekcjoner inny,
        int przesuniecieId,
        Interwal przesuniecieMomentuStartu)
    {
        super(inny, przesuniecieId, przesuniecieMomentuStartu);
    }

    @Override
    public boolean czyNowaTrasaLepsza(Trasa najlepszaTrasa, Trasa nowa) {
        if (licznikiPrzejazdow.get(najlepszaTrasa) < licznikiPrzejazdow)
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
