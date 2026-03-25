public class Macierz
{
    public static void main(String[] args)
    {
        int[][] macierz = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {0, 0, 0}};
        int[] tablica = new int[macierz.length * macierz[0].length];
        for (int i = 0; i < macierz.length; ++i)
            for (int j = 0; j < macierz[0].length; ++j)
                tablica[macierz[0].length * i + j] = macierz[i][j];
        int a = 1;
    }
}