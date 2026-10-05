public abstract class Funkcja extends Wyrażenie
{
    protected Wyrażenie arg;

    abstract protected String symbol();

    public String toString()
    {
        return symbol() + "(" + arg.toString() + ")";
    }
}
