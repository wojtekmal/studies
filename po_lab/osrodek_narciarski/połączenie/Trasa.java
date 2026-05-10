package połączenie;

import węzeł.Węzeł;

public class Trasa extends Połączenie
{
    private int poziom;
    private int czasPrzejazdu;
    private double bazowaAtrakcyjność;
    private double odporność;

    public Trasa(Węzeł początkowy, Węzeł końcowy, int poziom, int czasPrzejazdu,
        double bazowaAtrakcyjność, double odporność)
    {
        super(początkowy, końcowy);
        
        this.poziom = poziom;
        this.czasPrzejazdu = czasPrzejazdu;
        this. bazowaAtrakcyjność = bazowaAtrakcyjność;
        this.odporność = odporność;
    }
}
