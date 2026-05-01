public class Mnożenie extends Operator
{
    protected static final int priorytet = 1;

    public Mnożenie(Wyrażenie arg1, Wyrażenie arg2)
    {
        super(arg1, arg2);
    }

    public double wartość(double x)
    {
        return arg1.wartość(x) + arg2.wartość(x);
    }
}
