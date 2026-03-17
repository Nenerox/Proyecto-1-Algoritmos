package org.script;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<T> {
    // Atributo
    private Deque<T> stack;
    // Constructor
    public Stack() {
        this.stack = new ArrayDeque<>();
    }
    /**
     * Agrega un valor a la parte superior de la pila.
     * @param value valor parametrizado que se desea agregar a la pila
     */
    public void push(T value) {
        stack.push(value);
    }
    /**
     * Elimina y devuelve el valor en la parte superior de la pila. Si la pila está vacía, lanza una RuntimeException con un mensaje de error.
     * @return el valor en la parte superior de la pila
     * @exception RuntimeException si se intenta hacer pop en una pila vacía, con un mensaje de error indicando que la pila está vacía
     */
    public T pop() {
        try {
            return stack.pop();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Error al hacer pop: la pila está vacía", e);
        }
    }
    /**
     * Devuelve el valor en la parte superior de la pila sin eliminarlo. Si la pila está vacía, lanza una RuntimeException con un mensaje de error.
      * @return el valor en la parte superior de la pila
     */
    public T peek() {
        if (stack.peek() == null) {
            System.out.println("Advertencia: la pila está vacía");
        }
        return stack.peek();
    }
    /**
     * Método para verificar si el stack está vacío o no.
     * @return true si el stack está vacío o si el top del stack es un valor diferente de cero, false si el top del stack es cero
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    /**
     * Elimina todos los elementos de la pila, dejándola vacía.
     */
    public void clear() {   
        stack.clear();
    }
    /**
     * Para hacer una cadena que represente el contenido del stack
     * @return una cadena que representa el contenido de la pila, con el formato "TOP ( elemento1 >> elemento2 >> ... ) BOTTOM", donde elemento1 es el valor en la parte superior de la pila y los elementos siguientes son los valores debajo de él. Si un elemento es un arreglo de bytes, se muestra su representación hexadecimal.
     */
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
    /**
     * Convierte un arreglo de bytes a una cadena hexadecimal
     * @param bytes el arreglo de bytes a convertir
     * @return una cadena hexadecimal que representa el contenido del arreglo de bytes, donde cada byte se representa como dos dígitos hexadecimales
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}