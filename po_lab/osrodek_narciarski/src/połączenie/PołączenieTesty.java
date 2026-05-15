package połączenie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sportowiec.Sportowiec;

import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import struktury_danych.KolejkaNaWyciąg;
import struktury_danych.KolejkaZdarzeńLista;
import węzeł.Węzeł;
import zdarzenie.Godzina;
import zdarzenie.OdjazdTrasą;
import zdarzenie.PrzyjazdTrasą;
import zdarzenie.Zdarzenie;

@ExtendWith(MockitoExtension.class)
public class PołączenieTesty
{
    @Mock
    private Węzeł początkowy;
    @Mock
    private Węzeł końcowy;
    @Mock
    private KolejkaZdarzeńLista kolekjaZdarzeń;
    @Mock
    private KolejkaNaWyciąg kolejkaNaWyciąg;
    
    private Godzina godzina;
    @Mock
    private Sportowiec sportowiec;

    @BeforeEach
    public void inicjalizacja()
    {
        this.godzina = new Godzina("09:00:00");
    }
    
    @Test
    public void testujKonstruktor()
    {
        Trasa trasa = new Trasa(początkowy, końcowy, 1, 20, 0.5, 0.8, kolekjaZdarzeń, 0);
        verify(początkowy).dodajTrasę(trasa);
        assertEquals(trasa.bazowaAtrakcyjność(), 0.5);
        assertEquals(trasa.id(), 0);
        assertEquals(trasa.końcowy(), końcowy);
        assertEquals(trasa.liczbaPrzejazdów(), 0);
        assertEquals(trasa.poziom(), 1);
        assertEquals(trasa.odporność(), 0.8);
    }

    @Test
    public void testujWybierzPołączenieTrasa()
    {
        Trasa trasa = new Trasa(początkowy, końcowy, 1, 20, 0.5, 0.8, kolekjaZdarzeń, 0);
        ArgumentCaptor<OdjazdTrasą> porywaczOdjazdu = ArgumentCaptor.forClass(OdjazdTrasą.class);
        ArgumentCaptor<PrzyjazdTrasą> porywaczPrzyjazdu = ArgumentCaptor.forClass(PrzyjazdTrasą.class);

        trasa.wybierzPołączenie(godzina, sportowiec);

        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczOdjazdu.capture());
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczPrzyjazdu.capture());

        Zdarzenie odjazd = porywaczOdjazdu.getValue();
        Zdarzenie przyjazd = porywaczPrzyjazdu.getValue();

        assertEquals(odjazd.godzina(), godzina);
        assertEquals(przyjazd.godzina().toString(), odjazd.godzina().dodaj(20).toString());
    }

    @Test
    public void testujWybierzPołączenieWyciąg()
    {
        ArgumentCaptor<KolejkaNaWyciąg> porywaczKolejkiNaWyciąg = ArgumentCaptor.forClass(KolejkaNaWyciąg.class);
        Wyciąg wyciąg = new Wyciąg(początkowy, końcowy, 1, 4, 60, kolekjaZdarzeń, kolejkaNaWyciąg, 0);

        KolejkaNaWyciąg kolejkaNaWyciąg = verify()
        wyciąg.wybierzPołączenie(godzina, sportowiec);

        verify(kolekjaZdarzeń)

        Zdarzenie odjazd = porywaczOdjazdu.getValue();
        Zdarzenie przyjazd = porywaczPrzyjazdu.getValue();

        assertEquals(odjazd.godzina(), godzina);
        assertEquals(przyjazd.godzina().toString(), odjazd.godzina().dodaj(20).toString());
    }
}
