public class Zero extends Stała {
    private Zero()
    {
        super(0);
    }

    @Override
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
}
