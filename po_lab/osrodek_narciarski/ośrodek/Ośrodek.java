package ośrodek;

import java.util.Scanner;

import węzeł.Węzeł;
import zdarzenie.Godzina;
import zdarzenie.KolejkaZdarzeń;
import zdarzenie.KolejkaZdarzeńLista;
import połączenie.Wyciąg;
import sportowiec.GrupaSportowców;
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
        wyciągi = objektWczytujący.wyciągi(węzły);
        trasy = objektWczytujący.trasy(węzły);
        sportowcy = objektWczytujący.sportowcy(węzły);
    }

    private boolean jestPoCzasie(Zdarzenie zdarzenie)
    {
        Godzina koniecSymulacji = new Godzina("15:00:00");

        return koniecSymulacji.jestPrzed(zdarzenie.godzina());
    }

    public void rozpocznijSymulację()
    {
        wczytajDane();
        boolean poCzasie = false;

        while (!kolejkaZdarzeń.brakZdarzeń() && !poCzasie)
        {
            Zdarzenie zdarzenie = kolejkaZdarzeń.kolejneZdarzenie();

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
}
