package sportowiec;

import połączenie.Wyciąg;
import struktury_danych.KolejkaZdarzeń;
import węzeł.Węzeł;
import zdarzenie.Godzina;

public class Sportowiec
{
    private int poziom;
    private double spontaniczność;
    private boolean czyŚledzić;
    private double odwaga;
    private double wybredność;
    private Węzeł startowy;
    private Godzina godzinaStartu;
    private int id;

    public Sportowiec(int poziom, double spontaniczność, boolean czyŚledzić,
        double odwaga, double wybredność, Węzeł startowy, Godzina godzinaStartu,
        KolejkaZdarzeń kolejkaZdarzeń, int id)
    {
        this.poziom = poziom;
        this.spontaniczność = spontaniczność;
        this.czyŚledzić = czyŚledzić;
        this.odwaga = odwaga;
        this.wybredność = wybredność;
        this.startowy = startowy;
        this.godzinaStartu = godzinaStartu;
    }

    public int id()
    {
        return id;
    }

    public void przyjedźDo(Węzeł węzeł)
    {
        
    }
}
