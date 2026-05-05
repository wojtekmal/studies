package ośrodek;

import java.util.Scanner;

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

    private void wczytajSportowców(Scanner scannerWejścia)
    {
        Scanner scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaGrup = scannerLinii.nextInt();
        GrupaSportowców grupy = new GrupaSportowców[liczbaGrup];
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
            String godzinaStartu = scannerLinii.next();
            int odstępCzasowy = 0;

            Węzeł startowy = węzły[idStartowego];

            if (scannerLinii.hasNextInt())
            {
                odstępCzasowy = scannerLinii.nextInt();
            }

            grupy[i] = new GrupaSportowców(rozmiarGrupy, poziom, spontaniczność,
                czyŚledzić, odwaga, wybredność, startowy, godzinaStartu, 
                odstępCzasowy);
            
            liczbaSportowców += rozmiarGrupy;
        }

        sportowcy = new Sportowiec[liczbaSportowców];
        int idKolejnegoSportowca = 0;

        for (GrupaSportowców g : grupy)
        {
            Sportowiec[] sportowcyZGrupy = grupa.sportowcy();
            
            System.arraycopy(sportowcyZGrupy, 0, sportowcy,
                idKolejnegoSportowca, sportowcyZGrupy.length);
            
            idKolejnegoSportowca += sportowcyZGrupy.length;
        }
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

        kolejkaZdarzeń.rozpocznij();
    }
}
