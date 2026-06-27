package sportowcy.przeszukujacy;

import czas.Interwal;
import osrodek.krawedz.Trasa;
import sportowcy.ParametrySportowca;
import sportowcy.Sportowiec;

public class SportowiecZachlanny extends SportowiecPrzeszukujacy{

    public SportowiecZachlanny(ParametrySportowca parametry) {
        super(parametry);
    }

    public SportowiecZachlanny(Sportowiec doSkopiowania, int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        super(doSkopiowania, przesuniecieId, przesuniecieMomentuStartu);
    }

    @Override
    public boolean czyNowaTrasaLepsza(Trasa najlepszaTrasa, Trasa nowa) {
        return lacznaAtrakcyjnosc(nowa) > lacznaAtrakcyjnosc(najlepszaTrasa);
    }

    @Override
    public Sportowiec kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return new SportowiecZachlanny(this, przesuniecieId, przesuniecieMomentuStartu);
    }
}
