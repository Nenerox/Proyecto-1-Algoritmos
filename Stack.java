import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack {
    private Deque<byte[]> stack;

    public Stack() {
        this.stack = new ArrayDeque<>();
    }

    public void push(byte[] value) {
        stack.push(value);
    }

    public byte[] pop() {
        try {
            return stack.pop();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Error al hacer pop: la pila está vacía", e);
        }
    }

    public byte[] peek() {
        if (stack.peek() == null) {
            System.out.println("Advertencia: la pila está vacía");
        }
        return stack.peek();
    }

    public boolean peekBoolean() {
        byte[] top = peek();
        if(Arrays.equals(top, new byte[] {0})) {
            return false;
        } 
        return true;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    public void clear() {
        stack.clear();
    }

    //Para el metodo --trace Fase 2
    public String peekAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("TOP[");

        Iterator<byte[]> it = stack.iterator();
        while (it.hasNext()) {
            sb.append(Arrays.toString(it.next()));
            if (it.hasNext()) sb.append(", ");
        }

        sb.append("]BOTTOM");
        return sb.toString();
    }
}
