package sportowiec;

import java.util.Random;

import połączenie.Połączenie;
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
    private Węzeł pozycja;
    private Godzina godzinaStartu;
    private int id;
    private Random generator;

    public Sportowiec(int poziom, double spontaniczność, boolean czyŚledzić,
        double odwaga, double wybredność, Węzeł startowy, Godzina godzinaStartu, int id)
    {
        this.poziom = poziom;
        this.spontaniczność = spontaniczność;
        this.czyŚledzić = czyŚledzić;
        this.odwaga = odwaga;
        this.wybredność = wybredność;
        this.pozycja = startowy;
        this.godzinaStartu = godzinaStartu;

        generator = new Random();
    }

    public int id()
    {
        return id;
    }

    private void wylosujPołączenie(Trasa[] trasy, Wyciąg[] wyciągi)
    {
        int liczbaPołączeń = trasy.length + wyciągi.length;
        int wylosowanePołączenie = generator.nextInt(liczbaPołączeń);

        if (wylosowanePołączenie < trasy.length)
        {
            trasy[wylosowanePołączenie].wybierzPołączenie(this);
        }
        else
        {
            int indeksWyciągu = wylosowanePołączenie - trasy.length;
            wyciągi[indeksWyciągu].wybierzPołączenie(this);
        }
    }

    private double dopasowanieTrudności(Trasa trasa)
    {
        int poziomTrasy = trasa.poziom();
        double nadwyżkaPoziomu = poziomTrasy - poziom;
        double wynik;

        if (poziomTrasy >= poziom + 5)
        {
            wynik = 0;
        }
        else if (poziomTrasy >= poziom)
        {
            wynik = 1.0 - nadwyżkaPoziomu / 5;
        }
        else
        {
            wynik = Math.max(0.2, 1.0 + nadwyżkaPoziomu / 7);
        }

        return wynik;
    }

    private double atrakcyjność(Trasa trasa)
    {
        double dopasowanieTrudności = dopasowanieTrudności(trasa);
        double wyrównanieNawierzchnii = wyrównanieNawierzchnii(trasa);

        return odwaga * dopasowanieTrudności +
            wybredność * wyrównanieNawierzchnii;
    }

    public void przyjedźDo(Węzeł węzeł)
    {
        Trasa[] trasy = węzeł.trasy();
        Wyciąg[] wyciągi = węzeł.wyciągi();

        if (generator.nextDouble() < spontaniczność)
        {
            wylosujPołączenie(trasy, wyciągi);
        }
        else
        {
            double najlepszaAtrakcyjność = -1;
            Połączenie najlepszePołączenie;

            for (Trasa trasa : trasy)
            {
                double atrakcyjność = atrakcyjność(trasa);

                if (atrakcyjność > najlepszaAtrakcyjność)
                {
                    najlepszaAtrakcyjność = atrakcyjność;
                    najlepszePołączenie = trasa;
                }
            }

            for (Wyciąg wyciąg : wyciągi)
            {
                for (Trasa trasa : wyciąg.końcowy().trasy())
                {
                    double atrakcyjność = atrakcyjność(trasa);

                    if (atrakcyjność > najlepszaAtrakcyjność)
                    {
                        najlepszaAtrakcyjność = atrakcyjność;

                        // Podobieństwo tego kawałka kodu do poprzedniej pętli
                        // jest tylko pozorne - tutaj nas nie obchodzi która
                        // trasa na końcu danego wyciągu jest najlepsza, bo
                        // ustawiamy ten wyciąg jako najlepsze połączenie.
                        najlepszePołączenie = wyciąg;
                    }
                }
            }

            najlepszePołączenie.wybierzPołączenie(this);
        }
    }
}
