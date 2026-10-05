package zdarzenie;

import ośrodek.Ośrodek;

public class KoniecSymulacji extends Zdarzenie
{
    private Ośrodek ośrodek;

    public KoniecSymulacji(Ośrodek ośrodek)
    {
        super(new Godzina("15:00:00"));
        this.ośrodek = ośrodek;
    }

    public void wydarzSię()
    {
        ośrodek.zakończSymulację();
    }
}
