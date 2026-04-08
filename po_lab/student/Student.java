public class Student extends Osoba {
    private int indeks;
    private int rok;
    static final int MAKS_PRZEDMIOTY = 50;
    private String[] przedmioty;
    private int liczbaPrzedmiotów;

    public Student(String imie, String nazwisko)
    {
        super(imie, nazwisko);
        this.przedmioty = new String[MAKS_PRZEDMIOTY];
    }

    @Override
    public String toString() {
        String wynik = imieNazwisko() + " nr. indeksu: " + indeks + " rok: " + rok + " chodzi na:\n";
        for (String przedmiot : przedmioty)
        {
            wynik += przedmiot + "\n";
        }
        
    }
}
