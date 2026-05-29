import java.lang.reflect.InvocationTargetException;

public class Main
{
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DrzewoBST<WInteger> bst = new DrzewoBST<WInteger>();

        int[] doDrzewa = { 10, 5, 15, 2, 5, 7, 13, 21, 25 };
        for (int i : doDrzewa) bst.wstaw(new WInteger(i));

        bst.drukujWLinii();
        bst.drukujDrzewo(3);  // 3 to wielkość wcięcia na jednym poziomie
        System.out.println("Rozmiar: " + bst.rozmiar());
        System.out.println("Wysokość: " + bst.wysokość());
        System.out.println("Suma: " + bst.suma(WInteger.class));
        System.out.println("Znajdź 21: " + bst.znajdź(new WInteger(21)));
        System.out.println("Znajdź 0: " + bst.znajdź(new WInteger(0)));
        System.out.println("Znajdź 5: " + bst.znajdź(new WInteger(5)));
    }
}
