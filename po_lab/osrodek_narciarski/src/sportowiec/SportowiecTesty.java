package sportowiec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import połączenie.Trasa;
import węzeł.Węzeł;

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

    @Test
    public void testujPrzyjedźDo()
    {
        Węzeł węzeł = spy(new Węzeł(0, 0, 0, false, 0));
        Węzeł węzeł1 = spy(new Węzeł(0, 0, 0, false, 1));
        Węzeł węzeł2 = spy(new Węzeł(0, 0, 0, false, 2));
        Trasa trasa02 = spy(new Trasa(węzeł, węzeł2, 6, 1000, 0.9, 0.9, ))
        Sportowiec sportowiec = new Sportowiec(5, 0, false, 0.4, 0.3, 0);
    }
}
