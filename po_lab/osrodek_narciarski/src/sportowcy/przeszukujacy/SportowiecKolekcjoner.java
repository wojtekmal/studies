package sportowcy.przeszukujacy;

import czas.Interwal;
import osrodek.krawedz.Trasa;
import sportowcy.ParametrySportowca;
import sportowcy.Sportowiec;

import java.util.HashMap;
import java.util.Map;

public class SportowiecKolekcjoner extends SportowiecPrzeszukujacy{

    private final Map<Trasa, Integer> licznikiPrzejazdow = new HashMap<>();

    public SportowiecKolekcjoner(ParametrySportowca parametry) {
        super(parametry);
    }

    public SportowiecKolekcjoner(Sportowiec doSkopiowania, int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        super(doSkopiowania, przesuniecieId, przesuniecieMomentuStartu);
    }

    @Override
    public boolean czyNowaTrasaLepsza(Trasa najlepszaTrasa, Trasa nowa) {
        return licznikiPrzejazdow.getOrDefault(nowa, 0) <
            licznikiPrzejazdow.getOrDefault(najlepszaTrasa, 0);
    }

    @Override
    public Sportowiec kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return new SportowiecKolekcjoner(this, przesuniecieId, przesuniecieMomentuStartu);
    }

    @Override
    public void zanotujPrzejazdTrasa(Trasa trasa)
    {
        zanotujOstatnieOdwiedziny(trasa);

        int liczbaPrzejazdow = licznikiPrzejazdow.getOrDefault(trasa, 0);
        licznikiPrzejazdow.put(trasa, liczbaPrzejazdow + 1);
    }
}
