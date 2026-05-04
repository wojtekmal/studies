package ośrodek;

import java.util.Scanner;

public class Ośrodek
{
    private KolejkaZdarzeń kolejkaZdarzeń;
    private Węzeł[] węzły;
    private Wyciąg[] wyciągi;

    public Ośrodek()
    {
        kolejkaZdarzeń = new KolejkaZdarzeńLista();
    }

    private void wczytajDane()
    {
        Scanner scannerWejścia = new Scanner(System.in);
        Scanner scannerLinii;

        scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaWęzłów = scannerLinii.nextInt();
        węzły = new Węzeł[liczbaWęzłów];

        for (int i = 0; i < liczbaWęzłów; i++)
        {
            scannerLinii = new Scanner(scannerWejścia.nextLine());

            int wysokość, x, y;
            boolean jestStartowy;

            wysokość = scannerLinii.nextInt();
            x = scannerLinii.nextInt();
            y = scannerLinii.nextInt();
            jestStartowy = scannerLinii.hasNext();

            węzły[i] = new Węzeł(wysokość, x, y, jestStartowy);
        }

        scannerLinii = new Scanner(scannerWejścia.nextLine());
        int liczbaWyciągów = scannerLinii.nextInt();

    }

    public void rozpocznijSymulację()
    {
        wczytajDane();

        kolejkaZdarzeń.rozpocznij();
    }
}
