import java.util.Arrays;

public class Rectangles {
    public static int[] intersection(int[] a, int[] b)
    {
        int[] result = new int[4]; // x0, x1, y0, y1
        result[0] = Math.max(a[0], b[0]);
        result[1] = Math.min(a[1], b[1]);
        result[2] = Math.max(a[2], b[2]);
        result[3] = Math.min(a[3], b[3]);
        return result;
    }

    public static void test(int[] a, int[] b, int[] expected)
    {
        // TODO: accept different values for null rectangles.
        int[] result = intersection(a, b);
        if (Arrays.equals(result, expected)) System.out.println("OK");
        else System.out.println("WRONG ANSWER");
    }
    public static void main(String[] args)
    {
        test(new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0});
    }
}
