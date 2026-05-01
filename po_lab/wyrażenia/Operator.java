public abstract class Operator extends Wyrażenie
{
    protected Wyrażenie arg1;
    protected Wyrażenie arg2;

    protected Operator(Wyrażenie arg1, Wyrażenie arg2)
    {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
