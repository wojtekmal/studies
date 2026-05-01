public class Zero extends Stała {
    protected Zero()
    {
        super(0);
    }

    public static Stała twórz()
    {
        return new Zero();
    }

    @Override
    public Wyrażenie dodaj(Wyrażenie arg)
    {
        return arg;
    }

    @Override
    protected Wyrażenie dodajOdwrotnie(Wyrażenie arg)
    {
        return arg;
    }

    @Override
    public Wyrażenie pomnóżPrzez(Wyrażenie arg)
    {
        return this;
    }

    @Override
    protected Wyrażenie pomnóżPrzezOdwrotnie(Wyrażenie arg)
    {
        return this;
    }
}
