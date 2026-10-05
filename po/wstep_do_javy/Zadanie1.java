public class Zadanie1
{
    public static int nwd(int a, int b)
    {
        if (a < b) b ^= a ^= b; // Swap.

        while (b > 0)
        {
            a %= b;
            b ^= a ^= b;
        }

        return a;
    }
    public static void main(String[] args)
    {
        int a = 60;
        int b = 62;
        System.out.println(nwd(a, b));
    }
}