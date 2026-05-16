package sportowiec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SportowiecTesty
{
    @Test
    public void testujKonstruktor()
    {
        Sportowiec sportowiec = new Sportowiec(5, 0.9, false, 0.4, 0.3, 0);
        assertEquals(sportowiec.czyŚledzić(), false);
        assertEquals(sportowiec.id(), 0);
    }
}
