import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ZbiórTest
{
    @Test
    void testKonstruktorDomyślny()
    {
        Zbiór<Integer> zbiór = new Zbiór<>();

        assertTrue(zbiór.czyPusty());
        assertEquals(0, zbiór.rozmiar());
    }

    @Test
    void testKonstruktorKolekcyjny()
    {
        Zbiór<Integer> zbiór = new Zbiór<>
                (new ArrayList<>(Arrays.asList(1, 2, 3, 3)));

        assertEquals(3, zbiór.rozmiar());
        assertFalse(zbiór.czyPusty());
        assertTrue(zbiór.czyZawiera(3));
        assertFalse(zbiór.czyZawiera(4));
    }

    @Test
    void testKonstruktorParametryczny()
    {
        Zbiór<Integer> zbiór = new Zbiór<>(3, 3, 3);
        assertTrue(zbiór.czyZawiera(3));
        assertFalse(zbiór.czyZawiera(2));
        assertEquals(1, zbiór.rozmiar());
    }

    @org.junit.jupiter.api.Test
    void testToString()
    {
        Zbiór<Integer> zbiór = new Zbiór<Integer>();

        zbiór.wstaw(1);
        zbiór.wstaw(2);

        String wynikToString = zbiór.toString();

        assertEquals("[1, 2]", wynikToString);
    }

    @org.junit.jupiter.api.Test
    void czyPusty()
    {
        Zbiór<String> zbiór = new Zbiór<>("abc");
        assertFalse(zbiór.czyPusty());

        zbiór.usuń("Ala ma kota");
        assertFalse(zbiór.czyPusty());

        zbiór.usuń("abc");
        assertTrue(zbiór.czyPusty());

        zbiór.wstaw("67");
        assertFalse(zbiór.czyPusty());
    }

    @org.junit.jupiter.api.Test
    void testCzyZawieraElt()
    {
        Zbiór<Integer> zbiór = new Zbiór<>(-1);
        assertFalse(zbiór.czyZawiera(1));
        assertTrue(zbiór.czyZawiera(-1));
    }

    @org.junit.jupiter.api.Test
    void testRozmiar()
    {
        Zbiór<Integer> zbiór = new Zbiór<>(2, 1, 3, 7);
        assertEquals(4, zbiór.rozmiar());

        zbiór.wstaw(3);
        assertEquals(4, zbiór.rozmiar());

        zbiór.usuń(7);
        assertEquals(3, zbiór.rozmiar());
    }

    @Test
    void testWstaw()
    {
        Zbiór<Integer> zbiór = new Zbiór<>();
        zbiór.wstaw(5);
        assertTrue(zbiór.czyZawiera(5));

        zbiór.wstaw(5);
        assertTrue(zbiór.czyZawiera(5));
        assertFalse(zbiór.czyZawiera(0));
        assertEquals(1, zbiór.rozmiar());

        zbiór.wstaw(7);
        assertEquals(2, zbiór.rozmiar());
    }

    @org.junit.jupiter.api.Test
    void testUsuń()
    {
        Zbiór<Integer> zbiór = new Zbiór<>();
        zbiór.usuń(0);
        assertEquals(0, zbiór.rozmiar());

        zbiór.wstaw(0);
        assertEquals(1, zbiór.rozmiar());

        zbiór.usuń(0);
        assertTrue(zbiór.czyPusty());
    }

    @org.junit.jupiter.api.Test
    void testSuma()
    {
        Zbiór<Object> zbiór1 = new Zbiór<>(0, "", 0.0);
        Zbiór<Object> zbiór2 = new Zbiór<>(1, "1", 0.0);

        zbiór1.suma(zbiór2);
        assertEquals(new Zbiór<>(0, "", 1, "1", 0.0), zbiór1);
    }

    @org.junit.jupiter.api.Test
    void różnica()
    {
        Zbiór<Integer> zbiór1 = new Zbiór<>(0, 1, 2);
        Zbiór<Integer> zbiór2 = new Zbiór<>(2, 3, 4);

        zbiór1.różnica(zbiór2);
        assertEquals(new Zbiór<>(0, 1), zbiór1);

        zbiór2.różnica(zbiór2);
        assertEquals(new Zbiór<>(), zbiór2);
    }

    @org.junit.jupiter.api.Test
    void przecięcie()
    {
        Zbiór<Integer> zbiór1 = new Zbiór<>(1, 2, 3);
        Zbiór<Integer> zbiór2 = new Zbiór<>(0, 1);

        zbiór1.przecięcie(zbiór2);
        assertEquals(new Zbiór<Integer>(1), zbiór1);

        zbiór2.usuń(1);
        zbiór1.przecięcie(zbiór2);
        assertEquals(new Zbiór<>(), zbiór1);
    }

    @org.junit.jupiter.api.Test
    void testCzyZawieraZbiór()
    {
        Zbiór<Integer> zbiór1 = new Zbiór<>(1, 2, 3);
        Zbiór<Integer> zbiór2 = new Zbiór<>(0, 1);
        assertFalse(zbiór1.czyZawiera(zbiór2));

        zbiór2.usuń(0);
        assertTrue(zbiór1.czyZawiera(zbiór2));
    }

    @org.junit.jupiter.api.Test
    void czyZawartyW()
    {
        Zbiór<Integer> zbiór1 = new Zbiór<>(1, 2);
        Zbiór<Integer> zbiór2 = new Zbiór<>(0);
        assertFalse(zbiór2.czyZawartyW(zbiór1));

        zbiór2.usuń(0);
        assertTrue(zbiór2.czyZawartyW(zbiór1));
    }

    @org.junit.jupiter.api.Test
    void testEquals()
    {
        Zbiór<Integer> zbiór1 = new Zbiór<>(1, 2, 3);
        Zbiór<Integer> zbiór2 = new Zbiór<>(2, 1, 3);
        Zbiór<Double> zbiór3 = new Zbiór<>(1.0, 2.0, 3.0);

        assertEquals(zbiór1, zbiór2);
        assertNotEquals(zbiór1, zbiór3);
    }

    @org.junit.jupiter.api.Test
    void testHashCode()
    {
        Zbiór<Integer> zbiór1 = new Zbiór<>(1, 2, 3);
        Zbiór<Integer> zbiór2 = new Zbiór<>(2, 1, 3);
        Zbiór<Double> zbiór3 = new Zbiór<>(1.0, 2.0, 3.0);

        assertEquals(zbiór1.hashCode(), zbiór2.hashCode());
        assertNotEquals(zbiór1.hashCode(), zbiór3.hashCode());
    }
}