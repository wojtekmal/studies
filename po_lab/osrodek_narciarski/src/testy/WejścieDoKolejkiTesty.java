package testy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import połączenie.Wyciąg;
import sportowiec.Sportowiec;
import uk.org.webcompere.systemstubs.SystemStubs;
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
    public void testujZaraportuj() throws Exception
    {
        when(godzina.toString()).thenReturn("10:10:10");
        when(sportowiec.id()).thenReturn(3);
        when(wyciąg.id()).thenReturn(5);
        when(sportowiec.czyŚledzić()).thenReturn(true);

        WejścieDoKolejki wejścieDoKolejki = new WejścieDoKolejki(godzina, sportowiec, wyciąg);

        String raport = SystemStubs.tapSystemOut(() ->
        {
            wejścieDoKolejki.zaraportuj();
        });
        String oczekiwanyRaport = "10:10:10: Sportowiec 3 ustawił się w kolejce pod wyciągiem 5.\n";

        assertEquals(oczekiwanyRaport, raport);
    }
}
