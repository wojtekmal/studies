import java.util.Arrays;

public class SortRandomArray {
    public static void sort(int[] tab)
    {
        for (int i = 0; i < tab.length - 1; i++)
        {
            for (int j = 0; j < tab.length - 1; j++)
            {
                if (tab[j] > tab[j + 1])
                {
                    int temp = tab[j];
                    tab[j] = tab[j + 1];
                    tab[j + 1] = temp;
                }
            }
        }
    }

    public static void test(int[] tab, int[] expected)
    {
        sort(tab);
        if (Arrays.equals(tab, expected)) System.out.println("OK");
        else System.out.println("Skill issue.");
    }
    public static void main(String[] args)
    {
        test(new int[]{3, 1, 2, 4}, new int[]{1, 2, 3, 4});
    }
}
