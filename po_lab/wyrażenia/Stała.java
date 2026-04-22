public class Stała extends Wyrażenie {
    private double wartość;

    protected Stała(double x)
    {
        this.wartość = x;
    }

    static public Stała twórz(double x)
    {
        if (x == 0) return Zero.twórz();
        return new Stała(x);
    }

    @Override
    public double oblicz(double x)
    {
        return wartość;
    }

    @Override
    public Wyrażenie dodaj(Wyrażenie arg)
    {
        return arg.dodajStałą(this);
    }

    @Override
    protected Wyrażenie dodajStałą(Stała arg)
    {
        return Stała.twórz(wartość + arg.oblicz(0));
    }
}
