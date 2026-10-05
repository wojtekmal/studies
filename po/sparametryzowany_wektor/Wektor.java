import java.util.Arrays;

public class Wektor<T extends Ciało>
{
    public T[] współrzędne;

    public Wektor(T[] współrzędne)
    {
        this.współrzędne = współrzędne;
    }

    public String toString()
    {
        return Arrays.toString(współrzędne);
    }

    public T współrzędna(int k)
    {
        return współrzędne[k];
    }

    
}