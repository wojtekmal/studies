package testy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sportowiec.Sportowiec;
import struktury_danych.KolejkaNaWyciąg;

@ExtendWith(MockitoExtension.class)
public class KolejkaNaWyciągTesty
{
    @Mock
    private Sportowiec sportowiec1;
    @Mock
    private Sportowiec sportowiec2;
    @Mock
    private Sportowiec sportowiec3;
    @Mock
    private Sportowiec sportowiec4;

    @Test
    public void testujKonstruktor()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = new KolejkaNaWyciąg();
        assertTrue(kolejkaNaWyciąg.jestPusta());
    }

    @Test
    public void testujJestPusta()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = new KolejkaNaWyciąg();

        kolejkaNaWyciąg.dodaj(sportowiec1);
        assertFalse(kolejkaNaWyciąg.jestPusta());

        kolejkaNaWyciąg.dajKolejnego();
        assertTrue(kolejkaNaWyciąg.jestPusta());
    }

    @Test
    public void testujDodaj()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = new KolejkaNaWyciąg();
        kolejkaNaWyciąg.dodaj(sportowiec1);
        kolejkaNaWyciąg.dodaj(sportowiec2);
        kolejkaNaWyciąg.dodaj(sportowiec3);
        assertFalse(kolejkaNaWyciąg.jestPusta());
    }

    @Test
    public void testujDajKolejnego()
    {
        KolejkaNaWyciąg kolejkaNaWyciąg = new KolejkaNaWyciąg();
        kolejkaNaWyciąg.dodaj(sportowiec1);
        kolejkaNaWyciąg.dodaj(sportowiec2);
        kolejkaNaWyciąg.dodaj(sportowiec3);
        assertFalse(kolejkaNaWyciąg.jestPusta());

        Sportowiec pierwszy = kolejkaNaWyciąg.dajKolejnego();
        assertEquals(sportowiec1, pierwszy);

        Sportowiec drugi = kolejkaNaWyciąg.dajKolejnego();
        assertEquals(sportowiec2, drugi);
        assertFalse(kolejkaNaWyciąg.jestPusta());

        kolejkaNaWyciąg.dodaj(sportowiec4);
        Sportowiec trzeci = kolejkaNaWyciąg.dajKolejnego();
        assertEquals(sportowiec3, trzeci);
        assertFalse(kolejkaNaWyciąg.jestPusta());

        Sportowiec czwarty = kolejkaNaWyciąg.dajKolejnego();
        assertEquals(sportowiec4, czwarty);
        assertTrue(kolejkaNaWyciąg.jestPusta());
    }
}
