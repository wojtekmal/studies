package ośrodek;

import węzeł.Węzeł;
import zdarzenie.Godzina;
import połączenie.Wyciąg;
import sportowiec.Sportowiec;
import struktury_danych.KolejkaZdarzeń;
import struktury_danych.KolejkaZdarzeńLista;
import struktury_danych.KolejkaZdarzeń.BrakZdarzeń;
import połączenie.Trasa;
import zdarzenie.Zdarzenie;

public class Ośrodek
{
    private KolejkaZdarzeń kolejkaZdarzeń;
    private Węzeł[] węzły;
    private Wyciąg[] wyciągi;
    private Trasa[] trasy;
    private Sportowiec[] sportowcy;

    public Ośrodek()
    {
        kolejkaZdarzeń = new KolejkaZdarzeńLista();
    }

    private void wczytajDane()
    {
        KlasaWczytująca objektWczytujący = new KlasaWczytująca();

        węzły = objektWczytujący.węzły();
        wyciągi = objektWczytujący.wyciągi(węzły, kolejkaZdarzeń);
        trasy = objektWczytujący.trasy(węzły, kolejkaZdarzeń);
        sportowcy = objektWczytujący.sportowcy(węzły, kolejkaZdarzeń);
    }

    private boolean jestPoCzasie(Zdarzenie zdarzenie)
    {
        Godzina koniecSymulacji = new Godzina("16:00:00");

        return koniecSymulacji.jestPrzed(zdarzenie.godzina());
    }

    private void przeróbZdarzenia()
    {
        boolean poCzasie = false;

        while (!kolejkaZdarzeń.jestPusta() && !poCzasie)
        {
            Zdarzenie zdarzenie;
            try
            {
                zdarzenie = kolejkaZdarzeń.dajKolejne();
            }
            catch (BrakZdarzeń e)
            {
                System.err.println("Kolejka zdarzeń najpierw twierdzi, że nie jest pusta, a następnie nie podaje żadnego elementu.");
                zdarzenie = null;
            }

            if (jestPoCzasie(zdarzenie))
            {
                poCzasie = true;
            }
            else
            {
                zdarzenie.wydarzSię();
            }
        }
    }

    private void wypiszStatystyki()
    {
        for (Trasa trasa : trasy)
        {
            trasa.wypiszStatystyki();
        }

        for (Wyciąg wyciąg : wyciągi)
        {
            wyciąg.wypiszStatystyki();
        }
    }

    public void rozpocznijSymulację()
    {
        wczytajDane();
        
        przeróbZdarzenia();

        wypiszStatystyki();
    }
}
