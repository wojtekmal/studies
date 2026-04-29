package tests;

import onp.ONP;

public class Main {

    public static void main(String[] args)
    {
        try
        {
            System.out.println(ONP.oblicz("2 2 +"));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}