package onp;

import java.util.Scanner;

import stosy.IStos.PełnyStos;
import stosy.IStos.PustyStos;

public class ONP {
    public static class WyjątekONP extends Exception
    {
        public WyjątekONP(String message)
        {
            super(message);
        }

        public WyjątekONP()
        {
            super();
        }
    }
    public static class ZłeWyrażenie extends WyjątekONP
    {
        public ZłeWyrażenie(String message)
        {
            super(message);
        }

        public ZłeWyrażenie()
        {
            super();
        }
    }
    static class PusteWyrażenie extends WyjątekONP {}
    static class ZłyArgument extends ZłeWyrażenie
    {
        public ZłyArgument(String argument, int pozycja)
        {
            super("składnik: " + argument + ", pozycja: " +
                Integer.toString(pozycja) + "\n");
        }
    }
    static class ZaMałoArgumentów extends ZłeWyrażenie {}
    static class ZaDużoArgumentów extends ZłeWyrażenie {}

    public static final int rozmiarPamięciObliczeniowej = 100;

    public static int oblicz(String wyrażenie) throws WyjątekONP
    {
        stosy.IStos stos = new stosy.ProstyStos(rozmiarPamięciObliczeniowej);

        Scanner sc = new Scanner(wyrażenie);
        String słowo;

        for (int pozycja = 0; sc.hasNext(); pozycja++)
        {
            słowo = sc.next();

            if (słowo.matches("\\+-/\\*"))
            {
                int drugiArgument;
                int pierwszyArgument;

                try
                {
                    drugiArgument = stos.pop();
                    pierwszyArgument = stos.pop();
                }
                catch (PustyStos e)
                {
                    throw new ZaMałoArgumentów();
                }

                int wynikDziałania;

                if (słowo == "+")
                {
                    wynikDziałania = pierwszyArgument + drugiArgument;
                }
                else if (słowo == "-")
                {
                    wynikDziałania = pierwszyArgument - drugiArgument;
                }
                else if (słowo == "*")
                {
                    wynikDziałania = pierwszyArgument * drugiArgument;
                }
                else
                {
                    wynikDziałania = pierwszyArgument / drugiArgument;
                }

                try
                {
                    stos.push(wynikDziałania);
                }
                catch (PełnyStos e)
                {
                    throw new ZaDużoArgumentów();
                }
            }
            else
            {
                int liczba;

                try
                {
                    liczba = Integer.parseInt(słowo);
                }
                catch (Exception e)
                {
                    throw new ZłyArgument(słowo, pozycja);
                }

                try
                {
                    stos.push(liczba);
                }
                catch (PełnyStos e)
                {
                    throw new ZaDużoArgumentów();
                }
            }
        }

        int wynik;

        try
        {
            wynik = stos.pop();
        }
        catch (PustyStos e)
        {
            throw new PusteWyrażenie();
        }

        if (!stos.empty()) throw new ZaDużoArgumentów();

        return wynik;
    }
}