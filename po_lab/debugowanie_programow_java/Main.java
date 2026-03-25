public class Main {
    public static void main(String[] args) {
      int[] array = new int[10];
      for (int i = 0; i < 10; ++i) {
        array[i] = 7 * i;
      }
      System.out.println(array[9]);

      Point2D p = new Point2D(1.0f, -1.0f);
      System.out.println("(" + p.getX() + ", " + p.getY() + ")");
    }
}