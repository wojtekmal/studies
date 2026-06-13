import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ZbiórTest
{
    @Test
    void testKonstruktorDomyślny()
    {
        Zbiór<Integer> zbiór = new Zbiór<Integer>();

        assertEquals(true, zbiór.czyPusty());
        assertEquals(0, zbiór.rozmiar());
    }

    @Test
    void testKonstruktorKolekcyjny()
    {
        Zbiór<Integer> zbiór = new Zbiór<>
                (new ArrayList<>(Arrays.asList(1, 2, 3, 3)));

        assertEquals(3, zbiór.rozmiar());
        assertEquals(false, zbiór.czyPusty());
        assertEquals(true, zbiór.czyZawiera(3));
        assertEquals(false, zbiór.czyZawiera(4));
    }

    @Test
    void testKonstruktorParametryczny()
    {
        Zbiór<Integer> zbiór = new Zbiór(3, 3, 3);
        assertEquals(true, zbiór.czyZawiera(3));
        assertEquals(false, zbiór.czyZawiera(2));
        assertEquals(1, zbiór.rozmiar());
    }

    @org.junit.jupiter.api.Test
    void testToString()
    {
        Zbiór<Integer> zbiór = new Zbiór<Integer>();

        zbiór.wstaw(1);
        zbiór.wstaw(2);

        String wynikToString = zbiór.toString();

        assertEquals("{1, 2}", wynikToString);
    }

    @org.junit.jupiter.api.Test
    void czyPusty()
    {
        Zbiór<String> zbiór = new Zbiór<>("abc");
        assertEquals(false, zbiór.czyPusty());

        zbiór.usuń("Ala ma kota");
        assertEquals(false, zbiór.czyPusty());

        zbiór.usuń("abc");
        assertEquals(true, zbiór.czyPusty());

        zbiór.wstaw("67");
        assertEquals(false, zbiór.czyPusty());
    }

    @org.junit.jupiter.api.Test
    void testCzyZawieraElt()
    {
        Zbiór<Integer> zbiór = new Zbiór<>(-1);
        assertEquals(false, zbiór.czyZawiera(1));
        assertEquals(true, zbiór.czyZawiera(-1));
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
        assertEquals(true, zbiór.czyZawiera(5));

        zbiór.wstaw(5);
        assertEquals(true, zbiór.czyZawiera(5));
        assertEquals(false, zbiór.czyZawiera(0));
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
        assertEquals(true, zbiór.czyPusty());
    }

    @org.junit.jupiter.api.Test
    void testSuma()
    {
        Zbiór<Object> zbiór1 = new Zbiór<>(0, "", 0.0);
        Zbiór<Object> zbiór2 = new Zbiór<>(1, "1", 0.0);

        zbiór1.suma(zbiór2);
        assertEquals(zbiór1, new Zbiór<>(0, "", 1, "1", 0.0));
    }

    @org.junit.jupiter.api.Test
    void różnica()
    {
    }

    @org.junit.jupiter.api.Test
    void przecięcie()
    {
    }

    @org.junit.jupiter.api.Test
    void testCzyZawiera()
    {
    }

    @org.junit.jupiter.api.Test
    void czyZawartyW()
    {
    }

    @org.junit.jupiter.api.Test
    void testEquals()
    {
    }

    @org.junit.jupiter.api.Test
    void testHashCode()
    {
    }
}