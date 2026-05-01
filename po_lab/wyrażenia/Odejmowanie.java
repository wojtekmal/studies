public class Odejmowanie extends Operator
{
    @Override
    protected String znak()
    {
        return "-";
    }

    protected int priorytet()
    {
        return 0;
    }

    public Odejmowanie(Wyrażenie arg1, Wyrażenie arg2)
    {
        super(arg1, arg2);
    }

    public double wartość(double x)
    {
        return 0;
    }

    @Override
    public Wyrażenie pochodna()
    {
        return Zero.twórz();
    }
}
