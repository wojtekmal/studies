package ośrodek;

import java.util.Scanner;

import połączenie.Trasa;
import połączenie.Wyciąg;
import sportowiec.GrupaSportowców;
import sportowiec.Sportowiec;
import struktury_danych.KolejkaNaWyciąg;
import struktury_danych.KolejkaZdarzeń;
import węzeł.Węzeł;
import zdarzenie.Godzina;

public class KlasaWczytująca
{
    private Scanner scannerWejścia;

    public KlasaWczytująca()
    {
        scannerWejścia = new Scanner(System.in);
    }

    public Węzeł[] węzły()
    {
        Scanner scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaWęzłów = scannerLinii.nextInt();
        Węzeł[] węzły = new Węzeł[liczbaWęzłów];

        for (int i = 0; i < liczbaWęzłów; i++)
        {
            scannerLinii = new Scanner(scannerWejścia.nextLine());

            int wysokość = scannerLinii.nextInt();
            int x = scannerLinii.nextInt();
            int y = scannerLinii.nextInt();
            boolean jestStartowy = scannerLinii.hasNext();

            węzły[i] = new Węzeł(wysokość, x, y, jestStartowy, i);
        }

        scannerWejścia.nextLine();

        return węzły;
    }

    public Wyciąg[] wyciągi(Węzeł[] węzły, KolejkaZdarzeń kolejkaZdarzeń)
    {
        Scanner scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaWyciągów = scannerLinii.nextInt();
        Wyciąg[] wyciągi = new Wyciąg[liczbaWyciągów];

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
            KolejkaNaWyciąg kolejkaNaWyciąg = new KolejkaNaWyciąg();

            wyciągi[i] = new Wyciąg(końcowy, odstępCzasowy, rozmiarKrzesła, 
                czasPrzejazdu, kolejkaZdarzeń, kolejkaNaWyciąg, i);
            
            początkowy.dodajWyciąg(wyciągi[i]);
        }

        scannerWejścia.nextLine();

        return wyciągi;
    }

    public Trasa[] trasy(Węzeł[] węzły, KolejkaZdarzeń kolejkaZdarzeń)
    {
        Scanner scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaTras = scannerLinii.nextInt();
        Trasa[] trasy = new Trasa[liczbaTras];

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

            trasy[i] = new Trasa(końcowy, poziom, czasPrzejazdu,
                bazowaAtrakcyjność, odporność, kolejkaZdarzeń, i);
            
            początkowy.dodajTrasę(trasy[i]);
        }

        scannerWejścia.nextLine();

        return trasy;
    }

    public Sportowiec[] sportowcy(Węzeł[] węzły, KolejkaZdarzeń kolejkaZdarzeń)
    {
        Scanner scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaGrup = scannerLinii.nextInt();
        GrupaSportowców[] grupy = new GrupaSportowców[liczbaGrup];
        int liczbaSportowców = 0;

        for (int i = 0; i < liczbaGrup; i++)
        {
            scannerLinii = new Scanner(scannerWejścia.nextLine());
            int rozmiarGrupy = scannerLinii.nextInt();
            int poziom = scannerLinii.nextInt();
            double spontaniczność = scannerLinii.nextDouble();
            boolean czyŚledzić = scannerLinii.hasNext();

            scannerLinii = new Scanner(scannerWejścia.nextLine());
            double odwaga = scannerLinii.nextDouble();
            double wybredność = scannerLinii.nextDouble();

            scannerLinii = new Scanner(scannerWejścia.nextLine());
            int idStartowego = scannerLinii.nextInt();
            String godzinaStartuNapis = scannerLinii.next();
            int odstępCzasowy = 0;

            Węzeł startowy = węzły[idStartowego];
            Godzina godzinaStartu = new Godzina(godzinaStartuNapis);

            if (scannerLinii.hasNextInt())
            {
                odstępCzasowy = scannerLinii.nextInt();
            }

            grupy[i] = new GrupaSportowców(rozmiarGrupy, poziom, spontaniczność,
                czyŚledzić, odwaga, wybredność, startowy, godzinaStartu, 
                odstępCzasowy, liczbaSportowców);
            
            liczbaSportowców += rozmiarGrupy;
        }

        Sportowiec[] sportowcy = new Sportowiec[liczbaSportowców];
        int idKolejnegoSportowca = 0;

        for (GrupaSportowców grupa : grupy)
        {
            Sportowiec[] sportowcyZGrupy = grupa.sportowcy(kolejkaZdarzeń);
            
            System.arraycopy(sportowcyZGrupy, 0, sportowcy,
                idKolejnegoSportowca, sportowcyZGrupy.length);
            
            idKolejnegoSportowca += sportowcyZGrupy.length;
        }

        return sportowcy;
    }
}
