package zdarzenie;

public class Godzina
{
    private int godzina;
    private int minuta;
    private int sekunda;

    public Godzina(String napis)
    {
        godzina = Integer.parseInt(napis.substring(0, 2));
        minuta = Integer.parseInt(napis.substring(3, 5));
        sekunda = Integer.parseInt(napis.substring(6, 8));
    }

    private Godzina(int godzina, int minuta, int sekunda)
    {
        this.godzina = godzina;
        this.minuta = minuta;
        this.sekunda = sekunda;
    }

    public Godzina dodaj(int sekundy)
    {
        int nowa_sekunda = sekunda + sekundy;

        int nowa_minuta = minuta + nowa_sekunda / 60;
        nowa_sekunda %= 60;

        int nowa_godzina = godzina + nowa_minuta / 60;
        nowa_minuta %= 60;

        return new Godzina(nowa_godzina, nowa_minuta, nowa_sekunda);
    }

    public String toString()
    {
        return String.format("%02d:%02d:%02d", godzina, minuta, sekunda);
    }

    public boolean jestPrzed(Godzina inna)
    {
        if (godzina != inna.godzina)
        {
            return godzina < inna.godzina;
        }
        else
        {
            if (minuta != inna.minuta)
            {
                return minuta < inna.minuta;
            }
            else
            {
                return sekunda < inna.sekunda;
            }
        }
    }

    @Override
    public boolean equals(Object obiekt)
    {
        if (obiekt instanceof Godzina)
        {
            Godzina inna = (Godzina) obiekt;
            return inna.godzina == godzina && inna.minuta == minuta && 
                inna.sekunda == sekunda;
        }

        return false;
    }
}
