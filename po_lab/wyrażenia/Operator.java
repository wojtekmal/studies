public abstract class Operator extends Wyrażenie
{
    abstract String znak();

    protected Wyrażenie arg1;
    protected Wyrażenie arg2;

    protected Operator(Wyrażenie arg1, Wyrażenie arg2)
    {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String toString()
    {
        String wynik = "";

        String string1 = arg1.toString();
        String string2 = arg2.toString();

        if (arg1.priorytet() < priorytet())
        {
            wynik += "(" + string1 + ")";
        }
        else
        {
            wynik += string1;
        }

        wynik += " " + znak() + " (" + string2 + ")";
        return wynik;
    }
}
