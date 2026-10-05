package połączenie;

import sportowiec.Sportowiec;
import struktury_danych.KolejkaZdarzeń;
import węzeł.Węzeł;
import zdarzenie.Godzina;
import zdarzenie.OdjazdTrasą;
import zdarzenie.PrzyjazdTrasą;

public class Trasa extends Połączenie
{
    private int poziom;
    private int czasPrzejazdu;
    private double bazowaAtrakcyjność;
    private double odporność;

    public Trasa(Węzeł końcowy, int poziom, int czasPrzejazdu,
        double bazowaAtrakcyjność, double odporność,
        KolejkaZdarzeń kolejkaZdarzeń, int id)
    {
        super(końcowy, kolejkaZdarzeń, id);
        
        this.poziom = poziom;
        this.czasPrzejazdu = czasPrzejazdu;
        this. bazowaAtrakcyjność = bazowaAtrakcyjność;
        this.odporność = odporność;
        this.liczbaPrzejazdów = 0;
    }

    public int poziom()
    {
        return poziom;
    }

    public double bazowaAtrakcyjność()
    {
        return bazowaAtrakcyjność;
    }

    public double odporność()
    {
        return odporność;
    }

    public int liczbaPrzejazdów()
    {
        return liczbaPrzejazdów;
    }

    public void wybierzPołączenie(Godzina godzina, Sportowiec sportowiec)
    {
        OdjazdTrasą odjazd = new OdjazdTrasą(godzina, sportowiec, this);
        kolejkaZdarzeń.dodajZdarzenie(odjazd);

        PrzyjazdTrasą dojazd = new PrzyjazdTrasą(godzina.dodaj(czasPrzejazdu),
            sportowiec, this);
        kolejkaZdarzeń.dodajZdarzenie(dojazd);

        liczbaPrzejazdów++;
    }

    public void wypiszStatystyki()
    {
        String format = "Łączna liczba przejazdów trasą %d: %d.";
        System.out.println(String.format(format, id, liczbaPrzejazdów));
    }
}
