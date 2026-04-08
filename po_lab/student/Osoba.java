public class Osoba {
    private String imie;
    private String nazwisko;

    public Osoba(String imie, String nazwisko)
    {
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    public String imieNazwisko()
    {
        return imie + " " + nazwisko;
    }
}
