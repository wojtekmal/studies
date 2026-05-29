public class WInteger implements Dodawalny<WInteger>, Comparable<WInteger>
{
    private final int wartość;

    public WInteger()
    {
        this.wartość = 0;
    }

    public WInteger(int wartość)
    {
        this.wartość = wartość;
    }

    @Override
    public WInteger dodaj(WInteger inny)
    {
        return new WInteger(inny.wartość + wartość);
    }

    @Override
    public int compareTo(WInteger inny)
    {
        return Integer.compare(wartość, inny.wartość);
    }

    public String toString()
    {
        return Integer.toString(wartość);
    }
}
