package połączenie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sportowiec.Sportowiec;

import org.junit.jupiter.api.extension.ExtendWith;

import struktury_danych.KolejkaZdarzeńLista;
import węzeł.Węzeł;
import zdarzenie.Godzina;
import zdarzenie.OdjazdTrasą;
import zdarzenie.PrzyjazdTrasą;

@ExtendWith(MockitoExtension.class)
public class TrasaTesty
{
    @Mock
    private Węzeł końcowy;
    @Mock
    private KolejkaZdarzeńLista kolekjaZdarzeń;
    @Mock
    private Sportowiec sportowiec;
    
    @Test
    public void testujKonstruktor()
    {
        Trasa trasa = new Trasa(końcowy, 1, 20, 0.5, 0.8, kolekjaZdarzeń, 0);
        assertEquals(trasa.bazowaAtrakcyjność(), 0.5);
        assertEquals(trasa.id(), 0);
        assertEquals(trasa.końcowy(), końcowy);
        assertEquals(trasa.liczbaPrzejazdów(), 0);
        assertEquals(trasa.poziom(), 1);
        assertEquals(trasa.odporność(), 0.8);
    }

    @Test
    public void testujWybierzPołączenie()
    {
        Trasa trasa = new Trasa(końcowy, 1, 20, 0.5, 0.8, kolekjaZdarzeń, 0);
        ArgumentCaptor<OdjazdTrasą> porywaczOdjazdu = ArgumentCaptor.forClass(OdjazdTrasą.class);
        ArgumentCaptor<PrzyjazdTrasą> porywaczPrzyjazdu = ArgumentCaptor.forClass(PrzyjazdTrasą.class);

        trasa.wybierzPołączenie(new Godzina("09:00:00"), sportowiec);

        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczOdjazdu.capture());
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczPrzyjazdu.capture());

        OdjazdTrasą odjazdTrasą = porywaczOdjazdu.getValue();
        PrzyjazdTrasą przyjazdTrasą = porywaczPrzyjazdu.getValue();

        OdjazdTrasą oczekiwanyOdjazd = new OdjazdTrasą(new Godzina("09:00:00"), sportowiec, trasa);
        PrzyjazdTrasą oczekiwanyPrzyjazd = new PrzyjazdTrasą(new Godzina("09:00:20"), sportowiec, trasa);

        assertEquals(odjazdTrasą, oczekiwanyOdjazd);
        assertEquals(przyjazdTrasą, oczekiwanyPrzyjazd);
    }

    @AfterEach
    public void poWszystkich()
    {
        verifyNoMoreInteractions(końcowy, kolekjaZdarzeń, sportowiec);
    }
}