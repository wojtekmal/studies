package połączenie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
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
import zdarzenie.OdjazdKrzesełka;
import zdarzenie.OdjazdWyciągiem;
import zdarzenie.PrzyjazdWyciągiem;
import zdarzenie.WejścieDoKolejki;

@ExtendWith(MockitoExtension.class)
public class WyciągTesty
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
    public void testujKonstruktorWyciągu()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = mock(KolejkaNaWyciąg.class);
        ArgumentCaptor<OdjazdKrzesełka> porywaczOdjazdyKrzesełka = ArgumentCaptor.forClass(OdjazdKrzesełka.class);
        Wyciąg wyciąg = new Wyciąg(początkowy, końcowy, 30, 3, 500, kolekjaZdarzeń, kolejkaNaWyciąg, 0);

        verify(początkowy).dodajWyciąg(wyciąg);
        assertEquals(wyciąg.id(), 0);
        assertEquals(wyciąg.końcowy(), końcowy);
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczOdjazdyKrzesełka.capture());

        OdjazdKrzesełka odjazdKrzesełka = porywaczOdjazdyKrzesełka.getValue();
        OdjazdKrzesełka oczekiwanyOdjazdKrzesełka = new OdjazdKrzesełka(new Godzina("09:00:00"), wyciąg);

        assertEquals(odjazdKrzesełka, oczekiwanyOdjazdKrzesełka);
    }

    @Test
    public void testujWybierzPołączenieWyciąg()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = spy(new KolejkaNaWyciąg());
        Wyciąg wyciąg = new Wyciąg(początkowy, końcowy, 1, 4, 60, kolekjaZdarzeń, kolejkaNaWyciąg, 0);
        ArgumentCaptor<WejścieDoKolejki> porywaczWejściaDoKolejki = ArgumentCaptor.forClass(WejścieDoKolejki.class);

        wyciąg.wybierzPołączenie(new Godzina("09:00:00"), sportowiec);

        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczWejściaDoKolejki.capture());

        WejścieDoKolejki wejścieDoKolejki = porywaczWejściaDoKolejki.getValue();
        WejścieDoKolejki oczekiwaneWejścieDoKolejki = new WejścieDoKolejki(new Godzina("09:00:00"), sportowiec, wyciąg);

        assertEquals(wejścieDoKolejki, oczekiwaneWejścieDoKolejki);
    }

    @Test
    public void testujPrzyjmijZKolejki()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = spy(KolejkaNaWyciąg.class);
        Wyciąg wyciąg = new Wyciąg(początkowy, końcowy, 1, 4, 60, kolekjaZdarzeń, kolejkaNaWyciąg, 0);
        kolejkaNaWyciąg.dodaj(sportowiec);
        
        ArgumentCaptor<OdjazdWyciągiem> porywaczOdjazdu = ArgumentCaptor.forClass(OdjazdWyciągiem.class);
        ArgumentCaptor<PrzyjazdWyciągiem> porywaczPrzyjazdu = ArgumentCaptor.forClass(PrzyjazdWyciągiem.class);
        wyciąg.przyjmijZKolejki(new Godzina("09:00:00"));

        verify(kolejkaNaWyciąg).dajKolejnego();
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczOdjazdu.capture());
        verify(kolekjaZdarzeń).dodajZdarzenie(porywaczPrzyjazdu.capture());

        OdjazdWyciągiem odjazdWyciągiem = porywaczOdjazdu.getValue();
        PrzyjazdWyciągiem przyjazdWyciągiem = porywaczPrzyjazdu.getValue();

        OdjazdWyciągiem oczekiwanyOdjazd = new OdjazdWyciągiem(new Godzina("09:00:00"), sportowiec, wyciąg);
        PrzyjazdWyciągiem oczekiwanyPrzyjazd = new PrzyjazdWyciągiem(new Godzina("09:01:00"), sportowiec, wyciąg);

        assertEquals(odjazdWyciągiem, oczekiwanyOdjazd);
        assertEquals(przyjazdWyciągiem, oczekiwanyPrzyjazd);
    }
}
