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
        Scanner scannerWejścia = new Scanner(System.in);

        Scanner scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaWęzłów = scannerLinii.nextInt();
        węzły = new Węzeł[liczbaWęzłów];

        for (int i = 0; i < liczbaWęzłów; i++)
        {
            scannerLinii = new Scanner(scannerWejścia.nextLine());

            int wysokość = scannerLinii.nextInt();
            int x = scannerLinii.nextInt();
            int y = scannerLinii.nextInt();
            boolean jestStartowy = scannerLinii.hasNext();

            węzły[i] = new Węzeł(wysokość, x, y, jestStartowy);
        }

        scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaWyciągów = scannerLinii.nextInt();
        wyciągi = new Wyciąg[liczbaWyciągów];

        for (int i = 0; i < liczbaWyciągów; i++)
        {
            scannerLinii = new Scanner(scannerWejścia.nextLine());

            int idPoczątkowego = scannerLinii.nextInt();
            int idKońcowego = scannerLinii.nextInt();
            int odstępCzasowy = scannerLinii.nextInt();
            int rozmiarKrzesła = scannerLinii.nextInt();
            int czasPrzejazdu = scannerLinii.nextInt();

            Węzeł początkowy = węzły[idPoczątkowego];
            Węzeł końcowy = węzły[idKońcowego];

            wyciągi[i] = new Wyciąg(początkowy, końcowy, odstępCzasowy,
                rozmiarKrzesła, czasPrzejazdu);
        }

        scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaTras = scannerLinii.nextInt();
        trasy = new Trasa[liczbaTras];

        for (int i = 0; i < liczbaTras; i++)
        {
            scannerLinii = new Scanner(scannerWejścia.nextLine());

            int idPoczątkowego = scannerLinii.nextInt();
            int idKońcowego = scannerLinii.nextInt();
            int poziom = scannerLinii.nextInt();
            int czasPrzejazdu = scannerLinii.nextInt();
            double bazowaAtrakcyjność = scannerLinii.nextDouble();
            double odporność = scannerLinii.nextDouble();

            Węzeł początkowy = węzły[idPoczątkowego];
            Węzeł końcowy = węzły[idKońcowego];

            trasy[i] = new Trasa(początkowy, końcowy, poziom, czasPrzejazdu,
                bazowaAtrakcyjność, odporność);
        }

        wczytajSportowców(scannerWejścia);
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
