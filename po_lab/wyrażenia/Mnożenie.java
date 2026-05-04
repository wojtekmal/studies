public class Mnożenie extends Operator
{
    @Override
    protected String znak()
    {
        return "*";
    }
    
    protected int priorytet()
    {
        return 1;
    }

    protected Mnożenie(Wyrażenie arg1, Wyrażenie arg2)
    {
        super(arg1, arg2);
    }

    public double wartość(double x)
    {
        return arg1.wartość(x) * arg2.wartość(x);
    }

    public Wyrażenie pochodna()
    {
        return (arg1.pochodna().pomnóżPrzez(arg2)).dodaj
               (arg1.pomnóżPrzez(arg2.pochodna()));
    }
}
