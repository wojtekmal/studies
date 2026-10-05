public class Program {
    public static void main(String[] args)
    {
        Ułamek a = new Ułamek(1, 2);
        System.out.println(a);

        Ułamek b = new Ułamek(2, 1);
        System.out.println(b);

        Ułamek c = Ułamek.dodaj(a, b);
        System.out.println(c);

        Ułamek d = Ułamek.odejmij(a, b);
        System.out.println(d);

        d.odwróć();
        System.out.println(d);
        System.out.println(d.toDouble());

        a.podziel(b);
        System.out.println(a);

        d.pomnóż(a);
        System.out.println(d);

        System.out.println(a.czyMniejszeNiż(b));
        System.out.println(Ułamek.czyRówne(a, a));
    }
}
