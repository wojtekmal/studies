import java.util.Scanner;

public class LargestCliqueSize {
    private static int n, m;
    private static boolean[][] adjacent;

    private static void input()
    {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        adjacent = new boolean[n][n];

        for (int i = 0; i < m; i++)
        {
            int u, v;
            u = scanner.nextInt();
            v = scanner.nextInt();
            adjacent[u][v] = true;
            adjacent[v][u] = true;
        }
    }

    private static boolean check_mask(long mask)
    {
        for (int i = 0; i < n; i++)
        {
            if ((mask & (1L << i)) == 0) continue;
            
            for (int j = i + 1; j < n; j++)
            {
                if ((mask & (1L << j)) == 0) continue;
                if (!adjacent[i][j]) return false;
            }
        }

        return true;
    }

    private static void find_largest_clique_size()
    {
        input();
        int result = 0;

        for (long mask = 0; mask < 1L << n; mask++)
        {
            if (check_mask(mask)) result = Math.max(result, Long.bitCount(mask));
        }

        System.out.println(result);
    }

    public static void main(String[] args)
    {
        find_largest_clique_size();
    }
}
