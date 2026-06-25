package osrodek_narciarski.sportowcy;

import osrodek_narciarski.czas.Interwal;

public class GrupaSportowcow {

    private final Sportowiec schematSportowca;

    private final int krotnosc;

    private final Interwal odstepMiedzySportowcami;

    public int krotnosc() {
        return krotnosc;
    }

    public GrupaSportowcow(Sportowiec schematSportowca, int krotnosc, Interwal odstepMiedzySportowcami) {
        assert krotnosc > 0 : "Ilość sportowców w grupie musi być dodatnia";

        this.schematSportowca = schematSportowca;
        this.krotnosc = krotnosc;
        this.odstepMiedzySportowcami = odstepMiedzySportowcami;
    }

    public Sportowiec[] podajSportowcow() {
        Sportowiec[] sportowcy = new Sportowiec[krotnosc];
        sportowcy[0] = schematSportowca;

        for (int i = 1; i < krotnosc; i++) {
            sportowcy[i] = sportowcy[i - 1].kopia(1, odstepMiedzySportowcami);
        }

        return sportowcy;
    }

    @Override
    public String toString() {
        return "GrupaSportowcow [schematSportowca=" + schematSportowca + ", krotnosc=" + krotnosc
            + ", odstepMiedzySportowcami=" + odstepMiedzySportowcami + "]";
    }
}
