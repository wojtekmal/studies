package węzeł;

import java.util.Arrays;

import połączenie.Trasa;
import połączenie.Wyciąg;

public class Węzeł
{
    private int wysokość;
    private int x;
    private int y;
    private boolean jestStartowy;
    private int id;

    private Trasa[] trasy;
    private Wyciąg[] wyciągi;

    public Węzeł(int wysokość, int x, int y, boolean jestStartowy, int id)
    {
        this.wysokość = wysokość;
        this.x = x;
        this.y = y;
        this.jestStartowy = jestStartowy;
        this.id = id;

        this.trasy = new Trasa[0];
        this.wyciągi = new Wyciąg[0];
    }

    public void dodajTrasę(Trasa trasa)
    {
        // Poniższa implementacja jest kwadratowa od liczby tras, ale to nie
        // szkodzi, bo i tak raczej powinno być dużo więcej sportowców niż
        // tras i wyciągów w jednym węźle.

        Trasa[] noweTrasy = new Trasa[trasy.length + 1];
        System.arraycopy(trasy, 0, noweTrasy, 0, trasy.length);
        trasy = noweTrasy;

        trasy[trasy.length - 1] = trasa;
        System.out.println(Arrays.toString(trasy));
    }

    public void dodajWyciąg(Wyciąg wyciąg)
    {
        Wyciąg[] noweWyciągi = new Wyciąg[wyciągi.length + 1];
        System.arraycopy(wyciągi, 0, noweWyciągi, 0, wyciągi.length);
        wyciągi = noweWyciągi;

        wyciągi[wyciągi.length - 1] = wyciąg;
    }

    public Trasa[] trasy()
    {
        return trasy;
    }
    
    public Wyciąg[] wyciągi()
    {
        return wyciągi;
    }
}
