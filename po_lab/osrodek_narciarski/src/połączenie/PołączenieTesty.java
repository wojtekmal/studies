package połączenie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sportowiec.Sportowiec;

import org.junit.jupiter.api.extension.ExtendWith;

import struktury_danych.KolejkaNaWyciąg;
import struktury_danych.KolejkaZdarzeńLista;
import węzeł.Węzeł;
import zdarzenie.Godzina;
import zdarzenie.OdjazdTrasą;
import zdarzenie.OdjazdWyciągiem;
import zdarzenie.PrzyjazdTrasą;
import zdarzenie.PrzyjazdWyciągiem;

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
    private Sportowiec sportowiec;
    
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

        trasa.wybierzPołączenie(new Godzina("09:00:00"), sportowiec);

        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczOdjazdu.capture());
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczPrzyjazdu.capture());

        OdjazdTrasą odjazdTrasą = porywaczOdjazdu.getValue();
        PrzyjazdTrasą przyjazdTrasą = porywaczPrzyjazdu.getValue();

        OdjazdTrasą oczekiwanyOdjazd = new OdjazdTrasą(new Godzina("09:00:00"), sportowiec, trasa);
        PrzyjazdTrasą oczekiwanyPrzyjazd = new PrzyjazdTrasą(new Godzina("09:20:00"), sportowiec, trasa);

        assertEquals(odjazdTrasą, oczekiwanyOdjazd);
        assertEquals(przyjazdTrasą, oczekiwanyPrzyjazd);
    }

    @Test
    public void testujWybierzPołączenieWyciąg()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = spy(new KolejkaNaWyciąg());
        Wyciąg wyciąg = new Wyciąg(początkowy, końcowy, 1, 4, 60, kolekjaZdarzeń, kolejkaNaWyciąg, 0);

        wyciąg.wybierzPołączenie(new Godzina("09:00:00"), sportowiec);

        verify(kolejkaNaWyciąg).dodaj(sportowiec);
    }

    @Test
    public void testujPrzyjmijZKolejki()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = spy(new KolejkaNaWyciąg());
        Wyciąg wyciąg = new Wyciąg(początkowy, końcowy, 1, 4, 60, kolekjaZdarzeń, kolejkaNaWyciąg, 0);
        
        ArgumentCaptor<OdjazdWyciągiem> porywaczOdjazdu = ArgumentCaptor.forClass(OdjazdWyciągiem.class);
        ArgumentCaptor<PrzyjazdWyciągiem> porywaczPrzyjazdu = ArgumentCaptor.forClass(PrzyjazdWyciągiem.class);
        wyciąg.przyjmijZKolejki(new Godzina("09:00:00"));

        verify(kolejkaNaWyciąg).dajKolejnego();
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczOdjazdu.capture());
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczPrzyjazdu.capture());

        OdjazdWyciągiem odjazdWyciągiem = porywaczOdjazdu.getValue();
        PrzyjazdWyciągiem przyjazdWyciągiem = porywaczPrzyjazdu.getValue();

        OdjazdWyciągiem oczekiwanyOdjazd = new OdjazdWyciągiem(new Godzina("09:00:00"), sportowiec, wyciąg);
        PrzyjazdWyciągiem oczekiwanyPrzyjazd = new PrzyjazdWyciągiem(new Godzina("10:00:00"), sportowiec, wyciąg);

        assertEquals(odjazdWyciągiem, oczekiwanyOdjazd);
        assertEquals(przyjazdWyciągiem, oczekiwanyPrzyjazd);
    }
}
