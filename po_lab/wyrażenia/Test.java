public class Test
{
    public static void main(String[] args)
    {
        Wyrażenie sin = Sinus.twórz(Zmienna.twórz());
        Wyrażenie sin_kwadrat = sin.pomnóżPrzez(sin);
        Wyrażenie cos = Cosinus.twórz(Zmienna.twórz());
        Wyrażenie cos_kwadrat = cos.pomnóżPrzez(cos);
        Wyrażenie jedynka_trygonometryczna = sin_kwadrat.dodaj(cos_kwadrat);
        System.out.println("Mamy jedynkę trygonometryczną:");
        System.out.println(jedynka_trygonometryczna);
        System.out.println("Powinno wyjść 1.0.");
        System.out.println(jedynka_trygonometryczna.wartość(67));
        System.out.println("");

        System.out.println("Testujemy wymagane uproszczenia:");
        Wyrażenie zero = Zero.twórz();
        Wyrażenie jeden = Jeden.twórz();
        Wyrażenie dwa = Stała.twórz(2);
        Wyrażenie log = Logarytm.twórz(Zmienna.twórz());

        System.out.println("\nPowinno wyjść 4.0.");
        System.out.println(dwa.dodaj(dwa));

        System.out.println("\nPowinno wyjść log(x).");
        System.out.println(log.dodaj(zero));

        System.out.println("\nPowinno wyjść log(x).");
        System.out.println(zero.dodaj(log));

        System.out.println("\nPowinno wyjść 4.0.");
        System.out.println(dwa.pomnóżPrzez(dwa));

        System.out.println("\nPowinno wyjść 0.0.");
        System.out.println(log.pomnóżPrzez(zero));

        System.out.println("\nPowinno wyjść 0.0.");
        System.out.println(zero.pomnóżPrzez(log));

        System.out.println("\nPowinno wyjść log(x).");
        System.out.println(log.pomnóżPrzez(jeden));

        System.out.println("\nPowinno wyjść log(x).");
        System.out.println(jeden.pomnóżPrzez(log));
        
        System.out.println(
            "\nSprawdzamy całkę sinusa - powinna wynosić 1 na [0, pi)");
        System.out.println(sin.całka(0, Math.PI, 10000));
    }
}
