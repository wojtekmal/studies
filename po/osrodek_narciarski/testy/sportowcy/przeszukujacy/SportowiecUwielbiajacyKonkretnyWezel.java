package sportowcy.przeszukujacy;

import czas.Interwal;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import sportowcy.ParametrySportowca;
import sportowcy.Sportowiec;

public class SportowiecUwielbiajacyKonkretnyWezel extends SportowiecPrzeszukujacy {
    private final Wezel uwielbianyWezel;

    public SportowiecUwielbiajacyKonkretnyWezel(ParametrySportowca parametry, Wezel uwielbianyWezel) {
        super(parametry);
        this.uwielbianyWezel = uwielbianyWezel;
    }

    @Override
    public boolean czyNowaTrasaLepsza(Trasa najlepszaTrasa, Trasa nowa) {
        return nowa.poczatek() == uwielbianyWezel;
    }

    @Override
    public Sportowiec kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return null;
    }
}
