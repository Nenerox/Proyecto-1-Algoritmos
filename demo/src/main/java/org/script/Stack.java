package org.script;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<T> {
    private Deque<T> stack;

    public Stack() {
        this.stack = new ArrayDeque<>();
    }

    public void push(T value) {
        stack.push(value);
    }

    public T pop() {
        try {
            return stack.pop();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Error al hacer pop: la pila está vacía", e);
        }
    }

    public T peek() {
        if (stack.peek() == null) {
            System.out.println("Advertencia: la pila está vacía");
        }
        return stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    public void clear() {
        stack.clear();
    }

    //Para el metodo --trace Fase 2
    public String trace() {
        StringBuilder sb = new StringBuilder();
        sb.append("TOP ( ");

        Iterator<T> it = stack.iterator();
        while (it.hasNext()) {
            T valor = it.next();
            if (valor instanceof byte[]){
                sb.append(bytesToHex((byte[]) valor));
            } else {
                sb.append(valor);
            }

            if (it.hasNext()) sb.append(" >> ");
        }

        sb.append(" ) BOTTOM");
        return sb.toString();
    }

    private String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
        sb.append(String.format("%02X", b));
    }
    return sb.toString();
}
}