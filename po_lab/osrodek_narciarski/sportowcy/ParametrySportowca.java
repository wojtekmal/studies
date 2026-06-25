package osrodek_narciarski.sportowcy;

import osrodek_narciarski.czas.Interwal;
import osrodek_narciarski.czas.Moment;
import osrodek_narciarski.losowosc.MaszynaLosujaca;
import osrodek_narciarski.osrodek.Wezel;

public class ParametrySportowca {
    private final int id;

    private final int poziomZaawansowania; // {0, 1, ..., 10}

    private final double wspolczynnikSpontanicznosci; // [0, 1]

    private final double wspolczynnikZnudzenia; // [0, 1]

    private final double wagaTrudnosci; // [0, 1]

    private final double wagaNawierzchni; // [0, 1]

    private final double wagaZnudzenia; // [0, 1]

    private final boolean sledzony;

    private final Wezel wezelStartowy;

    private final Moment momentStartu;

    private final MaszynaLosujaca maszynaLosujaca;

    public ParametrySportowca(int id, int poziomZaawansowania, double wspolczynnikSpontanicznosci, double wspolczynnikZnudzenia, double wagaTrudnosci, double wagaNawierzchni, double wagaZnudzenia, boolean sledzony, Wezel wezelStartowy, Moment momentStartu, MaszynaLosujaca maszynaLosujaca) {
        this.id = id;
        this.poziomZaawansowania = poziomZaawansowania;
        this.wspolczynnikSpontanicznosci = wspolczynnikSpontanicznosci;
        this.wspolczynnikZnudzenia = wspolczynnikZnudzenia;
        this.wagaTrudnosci = wagaTrudnosci;
        this.wagaNawierzchni = wagaNawierzchni;
        this.wagaZnudzenia = wagaZnudzenia;
        this.sledzony = sledzony;
        this.wezelStartowy = wezelStartowy;
        this.momentStartu = momentStartu;
        this.maszynaLosujaca = maszynaLosujaca;
    }

    public int id() {
        return id;
    }

    public int poziomZaawansowania() {
        return poziomZaawansowania;
    }

    public double wspolczynnikSpontanicznosci() {
        return wspolczynnikSpontanicznosci;
    }

    public double wspolczynnikZnudzenia() {
        return wspolczynnikZnudzenia;
    }

    public double wagaTrudnosci() {
        return wagaTrudnosci;
    }

    public double wagaNawierzchni() {
        return wagaNawierzchni;
    }

    public double wagaZnudzenia() {
        return wagaZnudzenia;
    }

    public boolean sledzony() {
        return sledzony;
    }

    public Wezel wezelStartowy() {
        return wezelStartowy;
    }

    public Moment momentStartu() {
        return momentStartu;
    }

    public MaszynaLosujaca maszynaLosujaca() {
        return maszynaLosujaca;
    }

    public ParametrySportowca kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu)
    {
        return new ParametrySportowca(id + przesuniecieId,
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
}
