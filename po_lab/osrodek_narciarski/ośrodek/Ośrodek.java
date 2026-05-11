package ośrodek;

import java.util.Scanner;

import węzeł.Węzeł;
import zdarzenie.Godzina;
import połączenie.Wyciąg;
import sportowiec.GrupaSportowców;
import struktury_danych.KolejkaZdarzeń;
import struktury_danych.KolejkaZdarzeńLista;
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
        trasy = objektWczytujący.trasy(węzły);
        sportowcy = objektWczytujący.sportowcy(węzły, kolejkaZdarzeń);
    }

    private boolean jestPoCzasie(Zdarzenie zdarzenie)
    {
        Godzina koniecSymulacji = new Godzina("16:00:00");

        return koniecSymulacji.jestPrzed(zdarzenie.godzina());
    }

    public void rozpocznijSymulację()
    {
        wczytajDane();
        boolean poCzasie = false;

        while (!kolejkaZdarzeń.jestPusta() && !poCzasie)
        {
            Zdarzenie zdarzenie = kolejkaZdarzeń.dajKolejne();

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
