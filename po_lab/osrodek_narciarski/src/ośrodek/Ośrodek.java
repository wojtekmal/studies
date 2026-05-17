package ośrodek;

import węzeł.Węzeł;
import zdarzenie.KoniecSymulacji;
import połączenie.Wyciąg;
import struktury_danych.KolejkaZdarzeń;
import struktury_danych.KolejkaZdarzeńLista;
import struktury_danych.KolejkaZdarzeń.BrakZdarzeń;
import połączenie.Trasa;
import zdarzenie.Zdarzenie;

public class Ośrodek
{
    public static void main(String[] args)
    {
        Ośrodek ośrodek = new Ośrodek();
        ośrodek.rozpocznijSymulację();
    }

    private KolejkaZdarzeń kolejkaZdarzeń;
    private Węzeł[] węzły;
    private Wyciąg[] wyciągi;
    private Trasa[] trasy;
    private boolean symulacjaZakończona;

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
        objektWczytujący.sportowcy(węzły, kolejkaZdarzeń);

        symulacjaZakończona = false;
    }

    public void zakończSymulację()
    {
        symulacjaZakończona = true;
    }

    private void przeróbZdarzenia()
    {
        KoniecSymulacji koniecSymulacji = new KoniecSymulacji(this);
        kolejkaZdarzeń.dodajZdarzenie(koniecSymulacji);

        while (!symulacjaZakończona)
        {
            // Zdarzenie o klasie KoniecSymulacji zawsze powinno być ostatnim
            // z zrealizowanych, a ono ustawia koniecSymulacji, więc w teorii
            // nie trzeba sprawdzać czy kolejka jest pusta, więc warunek while
            // tego nie sprawdza, ale na wszelki wypadek poniżej jest łapany
            // błąd o braku kolejnych zdarzeń.
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

            zdarzenie.wydarzSię();
        }

        while (!kolejkaZdarzeń.jestPusta())
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

            zdarzenie.zaraportuj();
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
