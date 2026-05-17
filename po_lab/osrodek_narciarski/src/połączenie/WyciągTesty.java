package połączenie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.AfterEach;
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
import zdarzenie.OdjazdKrzesełka;
import zdarzenie.OdjazdWyciągiem;
import zdarzenie.PrzyjazdWyciągiem;
import zdarzenie.WejścieDoKolejki;

@ExtendWith(MockitoExtension.class)
public class WyciągTesty
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
        KolejkaNaWyciąg kolejkaNaWyciąg = mock(KolejkaNaWyciąg.class);
        ArgumentCaptor<OdjazdKrzesełka> porywaczOdjazduKrzesełka = ArgumentCaptor.forClass(OdjazdKrzesełka.class);
        Wyciąg wyciąg = new Wyciąg(końcowy, 30, 3, 500, kolekjaZdarzeń, kolejkaNaWyciąg, 9);

        assertEquals(wyciąg.id(), 9);
        assertEquals(wyciąg.końcowy(), końcowy);
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczOdjazduKrzesełka.capture());

        OdjazdKrzesełka odjazdKrzesełka = porywaczOdjazduKrzesełka.getValue();
        OdjazdKrzesełka oczekiwanyOdjazdKrzesełka = new OdjazdKrzesełka(new Godzina("09:00:00"), wyciąg);

        assertEquals(odjazdKrzesełka, oczekiwanyOdjazdKrzesełka);

        verifyNoMoreInteractions(kolejkaNaWyciąg);
    }

    @Test
    public void testujWybierzPołączenie()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = spy(new KolejkaNaWyciąg());
        Wyciąg wyciąg = new Wyciąg(końcowy, 1, 4, 60, kolekjaZdarzeń, kolejkaNaWyciąg, 0);
        verify(kolekjaZdarzeń).dodajZdarzenie(any(OdjazdKrzesełka.class));

        ArgumentCaptor<WejścieDoKolejki> porywaczWejściaDoKolejki = ArgumentCaptor.forClass(WejścieDoKolejki.class);

        wyciąg.wybierzPołączenie(new Godzina("11:00:00"), sportowiec);

        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczWejściaDoKolejki.capture());

        WejścieDoKolejki wejścieDoKolejki = porywaczWejściaDoKolejki.getValue();
        WejścieDoKolejki oczekiwaneWejścieDoKolejki = new WejścieDoKolejki(new Godzina("11:00:00"), sportowiec, wyciąg);

        assertEquals(wejścieDoKolejki, oczekiwaneWejścieDoKolejki);
        
        verifyNoMoreInteractions(kolejkaNaWyciąg);
    }

    @Test
    public void testujPrzyjmijZKolejki()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = spy(KolejkaNaWyciąg.class);
        Wyciąg wyciąg = new Wyciąg(końcowy, 1, 4, 60, kolekjaZdarzeń, kolejkaNaWyciąg, 0);
        verify(kolekjaZdarzeń).dodajZdarzenie(any(OdjazdKrzesełka.class));

        kolejkaNaWyciąg.dodaj(sportowiec);
        verify(kolejkaNaWyciąg).dodaj(sportowiec);
        
        ArgumentCaptor<OdjazdWyciągiem> porywaczOdjazdu = ArgumentCaptor.forClass(OdjazdWyciągiem.class);
        ArgumentCaptor<PrzyjazdWyciągiem> porywaczPrzyjazdu = ArgumentCaptor.forClass(PrzyjazdWyciągiem.class);
        wyciąg.przyjmijZKolejki(new Godzina("11:00:00"));

        verify(kolejkaNaWyciąg).dajKolejnego();
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczOdjazdu.capture());
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczPrzyjazdu.capture());

        OdjazdWyciągiem odjazdWyciągiem = porywaczOdjazdu.getValue();
        PrzyjazdWyciągiem przyjazdWyciągiem = porywaczPrzyjazdu.getValue();

        OdjazdWyciągiem oczekiwanyOdjazd = new OdjazdWyciągiem(new Godzina("11:00:00"), sportowiec, wyciąg);
        PrzyjazdWyciągiem oczekiwanyPrzyjazd = new PrzyjazdWyciągiem(new Godzina("11:01:00"), sportowiec, wyciąg);

        assertEquals(odjazdWyciągiem, oczekiwanyOdjazd);
        assertEquals(przyjazdWyciągiem, oczekiwanyPrzyjazd);

        verify(kolejkaNaWyciąg, atLeast(0)).jestPusta();
        verifyNoMoreInteractions(kolejkaNaWyciąg);
    }

    @AfterEach
    public void poWszystkich()
    {
        verifyNoMoreInteractions(końcowy, kolekjaZdarzeń, sportowiec);
    }
}
