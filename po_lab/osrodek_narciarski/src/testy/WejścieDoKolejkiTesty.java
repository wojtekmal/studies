package testy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;
import zdarzenie.Godzina;
import zdarzenie.WejścieDoKolejki;

@ExtendWith(MockitoExtension.class)
public class WejścieDoKolejkiTesty
{
    @Mock
    private Godzina godzina;
    @Mock
    private Sportowiec sportowiec;
    @Mock
    private Wyciąg wyciąg;

    @Test
    public void testujZaraportuj()
    {
        WejścieDoKolejki wejścieDoKolejki = new WejścieDoKolejki(godzina, sportowiec, wyciąg);
        wejścieDoKolejki.zaraportuj();

    }
}
