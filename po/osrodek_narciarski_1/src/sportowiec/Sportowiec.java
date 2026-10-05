package sportowiec;

import java.util.Random;

import połączenie.Połączenie;
import połączenie.Trasa;
import połączenie.Wyciąg;
import węzeł.Węzeł;
import zdarzenie.Godzina;

public class Sportowiec
{
    private int poziom;
    private double spontaniczność;
    private boolean czyŚledzić;
    private double odwaga;
    private double wybredność;
    private int id;
    private Random generator;

    public Sportowiec(int poziom, double spontaniczność, boolean czyŚledzić,
        double odwaga, double wybredność, int id)
    {
        this.poziom = poziom;
        this.spontaniczność = spontaniczność;
        this.czyŚledzić = czyŚledzić;
        this.odwaga = odwaga;
        this.wybredność = wybredność;
        this.id = id;

        generator = new Random();
    }

    public int id()
    {
        return id;
    }

    private Połączenie wylosujPołączenie(Trasa[] trasy, Wyciąg[] wyciągi)
    {
        int liczbaPołączeń = trasy.length + wyciągi.length;
        int indeksWylosowanego = generator.nextInt(liczbaPołączeń);
        Połączenie wynik = null;

        if (indeksWylosowanego < trasy.length)
        {
            wynik = trasy[indeksWylosowanego];
        }
        else
        {
            wynik = wyciągi[indeksWylosowanego - trasy.length];
        }

        return wynik;
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

    private double wyrównanieNawierzchnii(Trasa trasa)
    {
        double odporność = trasa.odporność();
        double bazowaAtrakcyjność = trasa.bazowaAtrakcyjność();
        double liczbaPrzejazdów = trasa.liczbaPrzejazdów();

        double zużycie = Math.pow(odporność, liczbaPrzejazdów);

        return bazowaAtrakcyjność + (1.0 - bazowaAtrakcyjność) * zużycie;
    }

    private double atrakcyjność(Trasa trasa)
    {
        double dopasowanieTrudności = dopasowanieTrudności(trasa);
        double wyrównanieNawierzchnii = wyrównanieNawierzchnii(trasa);

        return odwaga * dopasowanieTrudności +
            wybredność * wyrównanieNawierzchnii;
    }

    private Połączenie najlepszePołączenie(Trasa[] trasy, Wyciąg[] wyciągi)
    {
        double najlepszaAtrakcyjność = -1;
        Połączenie najlepszePołączenie = null;

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

        return najlepszePołączenie;
    }

    public void przyjedźDo(Węzeł węzeł, Godzina godzina)
    {
        Trasa[] trasy = węzeł.trasy();
        Wyciąg[] wyciągi = węzeł.wyciągi();
        Połączenie wybranePołączenie = null;

        if (generator.nextDouble() < spontaniczność)
        {
            wybranePołączenie = wylosujPołączenie(trasy, wyciągi);
        }
        else
        {
            wybranePołączenie = najlepszePołączenie(trasy, wyciągi);
        }

        wybranePołączenie.wybierzPołączenie(godzina, this);
    }

    public boolean czyŚledzić()
    {
        return czyŚledzić;
    }
}
