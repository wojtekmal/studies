public abstract class Wyrażenie {
    public abstract double oblicz(double x);

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
}
