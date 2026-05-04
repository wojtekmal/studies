public class Jeden extends Stała
{
    protected Jeden()
    {
        super(1);
    }

    static public Stała twórz()
    {
        return new Jeden();
    }

    @Override
    public Wyrażenie pomnóżPrzez(Wyrażenie arg)
    {
        return arg;
    }

    @Override
    protected Wyrażenie pomnóżPrzezOdwrotnie(Wyrażenie arg)
    {
        return arg;
    }
}
