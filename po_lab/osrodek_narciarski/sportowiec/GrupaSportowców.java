package sportowiec;

import węzeł.Węzeł;
import zdarzenie.Godzina;
import zdarzenie.KolejkaZdarzeń;

public class GrupaSportowców
{
    private int rozmiarGrupy;
    private int poziom;
    private double spontaniczność;
    private boolean czyŚledzić;
    private double odwaga;
    private double wybredność;
    private Węzeł startowy;
    private Godzina godzinaStartu;

    // Domyślnie 0 jeśli jest 1 sportowiec, ale to nie ma znaczenia, bo ta
    // wartość jest tylko wykorzystywana w pętli która liczy kolejne godziny
    // startu sportowców z grupy.
    private int odstępCzasowy;

    public GrupaSportowców(int rozmiarGrupy, int poziom, double spontaniczność,
        boolean czyŚledzić, double odwaga, double wybredność, Węzeł startowy, 
        Godzina godzinaStartu, int odstępCzasowy)
    {
        this.rozmiarGrupy = rozmiarGrupy;
        this.poziom = poziom;
        this.spontaniczność = spontaniczność;
        this.czyŚledzić = czyŚledzić;
        this.odwaga = odwaga;
        this.wybredność = wybredność;
        this.startowy = startowy;
        this.godzinaStartu = godzinaStartu;
        this.odstępCzasowy = odstępCzasowy;
    }

    public Sportowiec[] sportowcy(KolejkaZdarzeń kolejkaZdarzeń)
    {
        Sportowiec[] sportowcy = new Sportowiec[rozmiarGrupy];

        for (int i = 0; i < rozmiarGrupy; i++)
        {
            sportowcy[i]
        }
    }
}
