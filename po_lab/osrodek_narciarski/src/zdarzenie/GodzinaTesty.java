package zdarzenie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GodzinaTesty
{
    @Test
    public void testujKonstruktor()
    {
        Godzina dziesiąta = new Godzina("10:00:00");
        assertEquals(dziesiąta.toString(), "10:00:00");

        Godzina święta = new Godzina("21:37:00");
        assertEquals(święta.toString(), "21:37:00");
    }

    @Test
    public void testujDodaj()
    {
        Godzina teraz = new Godzina("09:30:45");
        assertEquals(teraz.dodaj(5).toString(), "09:30:50");
        assertEquals(teraz.dodaj(62).toString(), "09:31:47");
        assertEquals(teraz.dodaj(2 * 3600 + 40 * 60 + 20).toString(), "12:11:05");
    }

    @Test
    public void testujJestPrzed()
    {
        assertTrue((new Godzina("09:36:04")).jestPrzed(new Godzina("10:00:00")));
        
        assertFalse((new Godzina("16:38:18")).jestPrzed(new Godzina("16:38:18")));

        assertFalse((new Godzina("16:38:19")).jestPrzed(new Godzina("16:38:18")));
    }
}
