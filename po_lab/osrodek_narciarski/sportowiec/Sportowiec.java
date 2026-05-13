package sportowiec;

import java.util.Random;

import połączenie.Trasa;
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
    private Random generator;

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

        generator = new Random();
    }

    public int id()
    {
        return id;
    }

    public void przyjedźDo(Węzeł węzeł)
    {
        Trasa[] trasy = węzeł.trasy();
        Wyciąg[] wyciągi = węzeł.wyciągi();

        
    }
}
