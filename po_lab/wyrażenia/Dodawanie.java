public class Dodawanie extends Operator
{
    @Override
    protected String znak()
    {
        return "+";
    }

    protected int priorytet()
    {
        return 0;
    }

    public Dodawanie(Wyrażenie arg1, Wyrażenie arg2)
    {
        super(arg1, arg2);
    }

    public double wartość(double x)
    {
        return arg1.wartość(x) + arg2.wartość(x);
    }

    @Override
    public Wyrażenie pochodna()
    {
        return arg1.pochodna().dodaj(arg2.pochodna());
    }
}
