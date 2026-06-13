import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        Zbiór<Integer> zbiór = new Zbiór<Integer>(new ArrayList<Integer>())
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
    }

    @org.junit.jupiter.api.Test
    void czyZawiera()
    {
    }

    @org.junit.jupiter.api.Test
    void rozmiar()
    {
    }

    @org.junit.jupiter.api.Test
    void usuń()
    {
    }

    @org.junit.jupiter.api.Test
    void suma()
    {
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