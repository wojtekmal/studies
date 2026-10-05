public class Sinus extends Funkcja
{
    protected Sinus(Wyrażenie arg)
    {
        this.arg = arg;
    }

    public static Sinus twórz(Wyrażenie arg)
    {
        return new Sinus(arg);
    }

    public double wartość(double x)
    {
        return Math.sin(arg.wartość(x));
    }

    public Wyrażenie pochodna()
    {
        return Cosinus.twórz(arg).pomnóżPrzez(arg.pochodna());
    }

    protected String symbol()
    {
        return "sin";
    }
}
