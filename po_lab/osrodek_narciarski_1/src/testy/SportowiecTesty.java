package testy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import połączenie.Trasa;
import sportowiec.Sportowiec;
import węzeł.Węzeł;
import zdarzenie.Godzina;

@ExtendWith(MockitoExtension.class)
public class SportowiecTesty
{
    @Test
    public void testujKonstruktor()
    {
        Sportowiec sportowiec = new Sportowiec(5, 0.9, true, 0.4, 0.3, 3);
        assertEquals(sportowiec.czyŚledzić(), true);
        assertEquals(sportowiec.id(), 3);
    }

    @Test
    public void testujWybórMniejZużytejTrasy()
    {
        Sportowiec sportowiec = new Sportowiec(0, 0, false, 0.5, 0.5, 0);

        Węzeł węzeł = new Węzeł(0, 0, 0, false, 0);

        Trasa mniejZużyta = mock(Trasa.class);
        Trasa bardziejZużyta = mock(Trasa.class);

        when(mniejZużyta.bazowaAtrakcyjność()).thenReturn(0.0);
        when(mniejZużyta.odporność()).thenReturn(0.8);
        when(mniejZużyta.poziom()).thenReturn(0);
        when(bardziejZużyta.bazowaAtrakcyjność()).thenReturn(0.0);
        when(bardziejZużyta.odporność()).thenReturn(0.8);
        when(bardziejZużyta.poziom()).thenReturn(0);

        when(mniejZużyta.liczbaPrzejazdów()).thenReturn(0);
        when(bardziejZużyta.liczbaPrzejazdów()).thenReturn(1);

        węzeł.dodajTrasę(mniejZużyta);
        węzeł.dodajTrasę(bardziejZużyta);

        Godzina dziesiąta = new Godzina("10:00:00");
        sportowiec.przyjedźDo(węzeł, dziesiąta);

        verify(mniejZużyta).wybierzPołączenie(dziesiąta, sportowiec);
        verifyNoMoreInteractions(mniejZużyta, bardziejZużyta);
    }
}
