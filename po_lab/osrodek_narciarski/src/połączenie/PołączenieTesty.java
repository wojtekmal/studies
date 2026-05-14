package połączenie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.Assertions.*;

import struktury_danych.KolejkaZdarzeń;
import struktury_danych.KolejkaZdarzeńLista;
import węzeł.Węzeł;

public class PołączenieTesty
{
    @Mock
    private Węzeł początkowy;
    
    @Mock
    private Węzeł końcowy;
    
    @BeforeEach
    public void inicjalizacja()
    {
        początkowy = new Węzeł(0, 0, 0, false, 0);
        końcowy = new Węzeł(0, 0, 0, false, 1);
    }
    
    @Test
    public void testujKonstruktor()
    {
        KolejkaZdarzeń kolejkaZdarzeń = new KolejkaZdarzeńLista();
        Trasa trasa = new Trasa(początkowy, końcowy, 0, 0, 0, 0, kolejkaZdarzeń, 0);

    }
}
