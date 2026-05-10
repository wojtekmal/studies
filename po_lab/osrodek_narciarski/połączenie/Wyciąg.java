package połączenie;

import węzeł.Węzeł;

public class Wyciąg extends Połączenie
{
    private int odstępCzasowy;
    private int rozmiarKrzesła;
    private int czasPrzejazdu;

    public Wyciąg(Węzeł początkowy, Węzeł końcowy, int odstępCzasowy,
        int rozmiarKrzesła, int czasPrzejazdu)
    {
        super(początkowy, końcowy);
        
        this.odstępCzasowy = odstępCzasowy;
        this.rozmiarKrzesła = rozmiarKrzesła;
        this.czasPrzejazdu = czasPrzejazdu;
    }
}
