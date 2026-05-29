public class WString implements Dodawalny<WString>, Comparable<WString>
{
    private final String wartość;

    public WString()
    {
        this.wartość = "";
    }

    public WString(String wartość)
    {
        this.wartość = wartość;
    }

    @Override
    public WString dodaj(WString inny) {
        return new WString(wartość + inny.wartość);
    }

    @Override
    public int compareTo(WString wString) {
        return 0;
    }

    public String toString()
    {
        return wartość;
    }
}
