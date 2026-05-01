public class Stała extends Wyrażenie
{
    private double wartość;

    protected Stała(double x)
    {
        this.wartość = x;
    }

    public static Stała twórz(double x)
    {
        if (x == 0) return Zero.twórz();
        return new Stała(x);
    }

    @Override
    public double wartość(double x)
    {
        return wartość;
    }

    @Override
    public Wyrażenie pochodna()
    {
        return Zero.twórz();
    }

    @Override
    public String toString()
    {
        return Double.toString(wartość);
    }

    @Override
    public Wyrażenie dodaj(Wyrażenie arg)
    {
        return arg.dodajStałą(this);
    }

    @Override
    protected Wyrażenie dodajStałą(Stała arg)
    {
        return Stała.twórz(wartość + arg.wartość(0));
    }

    @Override
    public Wyrażenie pomnóżPrzez(Wyrażenie arg)
    {
        return arg.pomnóżPrzezStałą(this);
    }

    @Override
    protected Wyrażenie pomnóżPrzezStałą(Stała arg)
    {
        return Stała.twórz(wartość * arg.wartość(0));
    }
}
