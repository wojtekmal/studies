public class Zadanie2
{
    public static int fibonacci(int n)
    {
        int a = 0;
        int b = 1;

        for (int i = 0; i < n; i++)
        {
            if (a > Integer.MAX_VALUE - b)
            {
                System.out.println("Wyjście poza zakres!");
                return 0;
            }

            int c = a + b;
            a = b;
            b = c;
        }

        return a;
    }
    public static void main(String[] args)
    {
        System.out.println(fibonacci(0));
        System.err.println(fibonacci(5));
        System.out.println(fibonacci(100));
    }
}