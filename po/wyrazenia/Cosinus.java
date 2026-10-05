public class Cosinus extends Funkcja
{
    protected Cosinus(Wyrażenie arg)
    {
        this.arg = arg;
    }

    public static Cosinus twórz(Wyrażenie arg)
    {
        return new Cosinus(arg);
    }

    public double wartość(double x)
    {
        return Math.cos(arg.wartość(x));
    }

    public Wyrażenie pochodna()
    {
        return Stała.twórz(-1).pomnóżPrzez(Sinus.twórz(arg)).pomnóżPrzez(
            arg.pochodna());
    }

    protected String symbol()
    {
        return "cos";
    }
}
