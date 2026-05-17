package sportowiec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import połączenie.Trasa;
import struktury_danych.KolejkaZdarzeń;
import struktury_danych.KolejkaZdarzeńLista;
import węzeł.Węzeł;
import zdarzenie.EtapPodróży;
import zdarzenie.Godzina;
import zdarzenie.OdjazdKrzesełka;
import zdarzenie.OdjazdTrasą;
import zdarzenie.PrzyjazdTrasą;

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
    public void testujWybórMniejZużytejTrasy()
    {
        Sportowiec sportowiec = new Sportowiec(0, 0, false, 0.5, 0.5, 0);

        Węzeł węzeł = new Węzeł(0, 0, 0, false, 0);
        Węzeł węzeł1 = new Węzeł(0, 0, 0, false, 1);

        KolejkaZdarzeń kolekjaZdarzeń = mock(KolejkaZdarzeńLista.class);
        Trasa mniejZużyta = spy(new Trasa(węzeł1, 0, 0, 0, 0.5, kolekjaZdarzeń, 0));
        Trasa bardziejZużyta = spy(new Trasa(węzeł1, 0, 0, 0, 0.5, kolekjaZdarzeń, 0));

        węzeł.dodajTrasę(mniejZużyta);
        węzeł.dodajTrasę(bardziejZużyta);
        System.out.println(mniejZużyta);
        System.out.println(bardziejZużyta);

        Godzina dziewiąta = new Godzina("09:00:00");
        bardziejZużyta.wybierzPołączenie(dziewiąta, sportowiec);
        verify(bardziejZużyta).wybierzPołączenie(dziewiąta, sportowiec);
        assertEquals(bardziejZużyta.liczbaPrzejazdów(), 1);

        Godzina dziesiąta = new Godzina("10:00:00");
        sportowiec.przyjedźDo(węzeł, dziesiąta);

        verify(mniejZużyta).wybierzPołączenie(dziesiąta, sportowiec);

        verify(mniejZużyta, atLeast(0)).bazowaAtrakcyjność();
        verify(mniejZużyta, atLeast(0)).liczbaPrzejazdów();
        verify(mniejZużyta, atLeast(0)).odporność();
        verify(mniejZużyta, atLeast(0)).poziom();
        verify(bardziejZużyta, atLeast(0)).bazowaAtrakcyjność();
        verify(bardziejZużyta, atLeast(0)).liczbaPrzejazdów();
        verify(bardziejZużyta, atLeast(0)).odporność();
        verify(bardziejZużyta, atLeast(0)).poziom();
        verifyNoMoreInteractions(mniejZużyta, bardziejZużyta);
    }
}
