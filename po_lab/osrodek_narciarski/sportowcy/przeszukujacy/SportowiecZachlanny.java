package osrodek_narciarski.sportowcy.przeszukujacy;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.sportowcy.ParametrySportowca;
import osrodek_narciarski.sportowcy.Sportowiec;

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
