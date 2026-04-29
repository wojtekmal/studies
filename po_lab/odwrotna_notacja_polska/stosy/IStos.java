package stosy;

public interface IStos {
    class PełnyStos extends Exception {};
    class PustyStos extends Exception {};

    public void push(int x) throws PełnyStos;

    public int pop() throws PustyStos;

    public boolean empty();
}