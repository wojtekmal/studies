public abstract class Wyrażenie
{
    protected Wyrażenie() {};

    protected int priorytet()
    {
        return 1;
    }

    public abstract double wartość(double x);

    public abstract Wyrażenie pochodna();

    public abstract String toString();

    public double całka(double a, double b, int n)
    {
        double wynik = 0;
        double poprzedniaWartość = wartość(a);
        double odcinek = (b - a) / n;
        
        for (int i = 1; i <= n; i++)
        {
            double wartość_i = wartość(a + i * odcinek);
            wynik += (wartość_i + poprzedniaWartość) * odcinek / 2;
            poprzedniaWartość = wartość_i;
        }

        return wynik;
    }

    public Wyrażenie dodaj(Wyrażenie arg)
    {
        return arg.dodajOdwrotnie(this);
    }

    protected Wyrażenie dodajOdwrotnie(Wyrażenie arg)
    {
        return new Dodawanie(arg, this);
    }

    protected Wyrażenie dodajStałą(Stała arg)
    {
        return new Dodawanie(arg, this);
    }

    public Wyrażenie pomnóżPrzez(Wyrażenie arg)
    {
        return arg.pomnóżPrzezOdwrotnie(this);
    }

    protected Wyrażenie pomnóżPrzezOdwrotnie(Wyrażenie arg)
    {
        return new Mnożenie(arg, this);
    }

    protected Wyrażenie pomnóżPrzezStałą(Stała arg)
    {
        return new Mnożenie(arg, this);
    }

    public Wyrażenie odejmij(Wyrażenie arg)
    {
        return new Odejmowanie(this, arg);
    }

    public Wyrażenie podzielPrzez(Wyrażenie arg)
    {
        return new Dzielenie(this, arg);
    }
}
