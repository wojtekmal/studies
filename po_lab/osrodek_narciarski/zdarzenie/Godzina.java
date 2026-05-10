package zdarzenie;

public class Godzina
{
    private int godzina;
    private int minuta;
    private int sekunda;

    public Godzina(String napis)
    {
        this.godzina = Integer.parseInt(napis.substring(0, 2));
        this.minuta = Integer.parseInt(napis.substring(3, 5));
        this.sekunda = Integer.parseInt(napis.substring(6, 8));
    }

    public void dodaj(int sekundy)
    {
        sekunda += sekundy;

        minuta += sekunda / 60;
        sekunda %= 60;

        godzina += minuta / 60;
        minuta %= 60;
    }

    public String toString()
    {
        return String.format("%02d:%02d:%02d", godzina, minuta, sekunda);
    }

    public int godzina()
    {
        return godzina;
    }

    public int minuta()
    {
        return minuta;
    }

    public int sekunda()
    {
        return sekunda;
    }

    public boolean jestPrzed(Godzina inna)
    {
        if (godzina != inna.godzina())
        {
            return godzina < inna.godzina();
        }
        else
        {
            if (minuta != inna.minuta())
            {
                return minuta < inna.minuta();
            }
            else
            {
                // Jeśli zdarzenia są w tej samej chwili, to nowe zdarzenie,
                // które się przyrównuje do innych, ma być później.
                return sekunda < inna.sekunda();
            }
        }
    }
}
