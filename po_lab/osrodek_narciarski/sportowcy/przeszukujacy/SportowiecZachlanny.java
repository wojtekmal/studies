package osrodek_narciarski.sportowcy.przeszukujacy;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.losowosc.MaszynaLosujaca;
import osrodek_narciarski.osrodek.Wezel;
import osrodek_narciarski.osrodek.krawedz.Trasa;
import osrodek_narciarski.sportowcy.Sportowiec;

public class SportowiecZachlanny extends SportowiecPrzeszukujacy{

    public SportowiecZachlanny(int id,
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

    public SportowiecZachlanny(SportowiecZachlanny inny,
        int przesuniecieId,
        Interwal przesuniecieMomentuStartu)
    {
        super(inny, przesuniecieId, przesuniecieMomentuStartu);
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
