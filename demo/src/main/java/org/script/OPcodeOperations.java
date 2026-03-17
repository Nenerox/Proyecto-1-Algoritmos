package org.script;

import java.util.Arrays;

public class OPcodeOperations {
    
    /**
     *  Valor false en el script
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_0(Stack<byte[]> ScriptStack) {
        ScriptStack.push(new byte[] {0});
    }
    /**
     * Valor true en el script
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_1(Stack<byte[]> ScriptStack) {
        ScriptStack.push(new byte[] {1});
    }
    /**
     * Valor entre 2 y 16 en el script, dependiendo del número dado como argumento. Si el número no está entre 2 y 16, se lanza una excepción IllegalArgumentException.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     * @param n el número que representa el valor a pushear
     * @exception IllegalArgumentException si el número no está entre 2 y 16
     */
    public void OP_2_16(Stack<byte[]> ScriptStack, byte n) {
        if (n < 2 || n > 16) {
            throw new IllegalArgumentException("numero debe estar entre 2 y 16");
        }
        ScriptStack.push(new byte[] {n});
    }
    /**
     * Pushea datos en la pila
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     * @param data los datos a pushear en la pila
     */
    public void OP_PUSHDATA(Stack<byte[]> ScriptStack, byte[] data) {
        ScriptStack.push(data);
    }
    /**
     * Duplica el valor que está en la parte superior del Stack
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_DUP(Stack<byte[]> ScriptStack) {
        byte[] top = ScriptStack.peek();
        ScriptStack.push(top);
    }
    /**
     * Elimina el valor que está en la parte superior del Stack
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_DROP(Stack<byte[]> ScriptStack) {
        ScriptStack.pop();
    }
    /**
     * Compara dos valores que están en la parte superior del stack, si son iguales pushea 1 (true) y si son diferentes pushea 0 (false)
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_EQUAL(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();

        if (Arrays.equals(a, b)) {
            ScriptStack.push(new byte[] {1});
        } else {
            ScriptStack.push(new byte[] {0});
        }
    }
    /**
     * Compara dos valores que están en la parte superior del stack, si son iguales pushea 1 (true) y si son diferentes pushea 0 (false). Si la comparación falla, lanza una excepción RuntimeException.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     * @exception RuntimeException si los valores comparados no son iguales
     */
    public void OP_EQUALVERIFY(Stack<byte[]> ScriptStack) {
        OP_EQUAL(ScriptStack);
        if (!popBoolean(ScriptStack)) {
            throw new RuntimeException("OP_EQUALVERIFY failed");
        }
    }
    /**
     * Extrae un valor booleano del stack.
     * @param stack el stack de datos
     * @return el valor booleano extraído
     */
    private boolean popBoolean(Stack<byte[]> stack) {
        byte[] top = stack.pop();
        return !Arrays.equals(top, new byte[]{0});
    }
    /**
     * Hasheo con 160 bits (RIPEMD-160) sobre los datos en la cima de la pila.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_HASH160(Stack<byte[]> ScriptStack) {
        byte[] data = ScriptStack.pop();
        byte[] hash = CryptoOperations.hash160(data);
        ScriptStack.push(hash);
    }

        /**
     * Realiza un hash SHA-256 sobre los datos en la cima de la pila.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_SHA256(Stack<byte[]> ScriptStack) {
        byte[] data = ScriptStack.pop();
        byte[] hash = CryptoOperations.hash256(data);
        ScriptStack.push(hash);
    }

    /**
     * Realiza un hash SHA-256 doble sobre los datos en la cima de la pila.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_HASH256(Stack<byte[]> ScriptStack) {
        byte[] data = ScriptStack.pop();
        byte[] hash = CryptoOperations.hash256(data);
        ScriptStack.push(hash);
    }
    
    /**
     * Verifica la firma digital de los datos en la cima de la pila.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_CHECKSIG(Stack<byte[]> ScriptStack) {
        byte[] pubKey = ScriptStack.pop();
        byte[] signature = ScriptStack.pop();
        
        boolean isValid = CryptoOperations.checkSignature(signature, pubKey);
        
        if (isValid) {
            ScriptStack.push(new byte[] {1});
        } else {
            ScriptStack.push(new byte[] {0});
        }
    }
    /**
     * Verifica la firma digital de los datos en la cima de la pila. Si la verificación falla, lanza una excepción RuntimeException.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     * @exception RuntimeException si la verificación de la firma falla
     */
    public void OP_CHECKSIGVERIFY(Stack<byte[]> ScriptStack) {
        OP_CHECKSIG(ScriptStack);
        if (!popBoolean(ScriptStack)) {
            throw new RuntimeException("OP_CHECKSIGVERIFY failed");
        }
    }
    /**
     * Intercambia los dos valores que están en la parte superior del stack
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */  
    public void OP_SWAP(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        ScriptStack.push(a);
        ScriptStack.push(b);
    }
    /**
     * Copia el segundo valor desde la parte superior del stack y lo coloca en la cima del stack, sin eliminar el valor original. Por ejemplo, si el stack tiene [A, B] (donde A es el valor más alto), después de OP_OVER el stack quedaría [A, B, A].
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_OVER(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.peek();
        ScriptStack.push(a);
        ScriptStack.push(b);
    }
    /**
     * Realiza una operación lógica NOT sobre el valor en la cima del stack. Si el valor es falso (0 o un arreglo de bytes vacío), se pushea 1 (true) al stack. Si el valor es verdadero (cualquier valor diferente de cero), se pushea 0 (false) al stack.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_NOT(Stack<byte[]> ScriptStack) {
        byte[] value = ScriptStack.pop();
        if (value.length == 0 || (value.length == 1 && value[0] == 0)) {
            ScriptStack.push(new byte[] {1});
        } else {
            ScriptStack.push(new byte[] {0});
        }
    }
    /**
     * Realiza una operación lógica AND entre los dos valores en la cima del stack. Si ambos valores son verdaderos (diferentes de cero), se pushea 1 (true) al stack. Si alguno de los valores es falso (0 o un arreglo de bytes vacío), se pushea 0 (false) al stack.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_BOOLAND(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        boolean aVal = !Arrays.equals(a, new byte[]{0});
        boolean bVal = !Arrays.equals(b, new byte[]{0});
        if (aVal && bVal) {
            ScriptStack.push(new byte[]{1});
        } else {
            ScriptStack.push(new byte[]{0});
        }
    }
    /**
     * Realiza una operación lógica OR entre los dos valores en la cima del stack. Si al menos uno de los valores es verdadero (diferente de cero), se pushea 1 (true) al stack. Si ambos valores son falsos (0 o un arreglo de bytes vacío), se pushea 0 (false) al stack.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_BOOLOR(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        boolean aVal = !Arrays.equals(a, new byte[]{0});
        boolean bVal = !Arrays.equals(b, new byte[]{0});
        if (aVal || bVal) {
            ScriptStack.push(new byte[]{1});
        } else {
            ScriptStack.push(new byte[]{0});
        }
    }
    /**
     * Se suman dos valores que están encima del stack
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_ADD(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        byte[] result = new byte[Math.max(a.length, b.length) + 1];
        int carry = 0;
        for (int i = 0; i < result.length; i++) {
            int sum = carry;
            if (i < a.length) sum += a[i] & 0xFF;
            if (i < b.length) sum += b[i] & 0xFF;
            result[i] = (byte) (sum & 0xFF);
            carry = sum >> 8;
        }
        int len = result.length;
        while (len > 1 && result[len - 1] == 0) len--;
        ScriptStack.push(Arrays.copyOf(result, len));
    }
    /**
     * Se restan dos valores que están encima del stack.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_SUB(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        byte[] result = new byte[Math.max(a.length, b.length) + 1];
        int borrow = 0;
        for (int i = 0; i < result.length; i++) {
            int diff = (b.length > i ? b[i] & 0xFF : 0) - (a.length > i ? a[i] & 0xFF : 0) - borrow;
            if (diff < 0) {
                diff += 256;
                borrow = 1;
            } else {
                borrow = 0;
            }
            result[i] = (byte) (diff & 0xFF);
        }
        int len = result.length;
        while (len > 1 && result[len - 1] == 0) len--;
        ScriptStack.push(Arrays.copyOf(result, len));
    }
    /**
     * Compara dos valores que están en la parte superior del stack, si son iguales pushea 1 (true) y si son diferentes pushea 0 (false). Si la comparación falla, lanza una excepción RuntimeException.
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     * @exception RuntimeException si los valores comparados no son iguales
     */
    public void OP_NUMEQUALVERIFY(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        if (Arrays.equals(a, b)) {
            ScriptStack.push(new byte[] {1});
        } else {
            ScriptStack.push(new byte[] {0});
        }
        if (!popBoolean(ScriptStack)) {
            throw new RuntimeException("OP_NUMEQUALVERIFY failed");
        }
    }
    /**
     * Compara dos valores que están encima del stack, si el segundo es menor que el primero pushea 1 (true) y si no, pushea 0 (false)
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_LESSTHAN(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        int aVal = bytesToInt(a);
        int bVal = bytesToInt(b);
        if (bVal < aVal) {
            ScriptStack.push(new byte[]{1});
        } else {
            ScriptStack.push(new byte[]{0});
        }
    }
    /**
     * Compara dos valores que están encima del stack, si el segundo es mayor que el primero pushea 1 (true) y si no, pushea 0 (false)
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_GREATERTHAN(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        int aVal = bytesToInt(a);
        int bVal = bytesToInt(b);
        if (bVal > aVal) {
            ScriptStack.push(new byte[]{1});
        } else {
            ScriptStack.push(new byte[]{0});
        }
    }
    /**
     * Compara dos valores que están encima del stack, si el segundo es menor o igual que el primero pushea 1 (true) y si no, pushea 0 (false)
     * @param ScriptStack el stack de datos actual sobre el cual se ejecuta la operación
     */
    public void OP_LESSTHANOREQUAL(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        int aVal = bytesToInt(a);
        int bVal = bytesToInt(b);
        if (bVal <= aVal) {
            ScriptStack.push(new byte[]{1});
        } else {
            ScriptStack.push(new byte[]{0});
        }
    }
    /**
     * Compara dos valores que están encima del stack, si el segundo es mayor o igual que el primero pushea 1 (true) y si no, pushea 0 (false)
     * @param ScriptStack
     */
    public void OP_GREATERTHANOREQUAL(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        int aVal = bytesToInt(a);
        int bVal = bytesToInt(b);
        if (bVal >= aVal) {
            ScriptStack.push(new byte[]{1});
        } else {
            ScriptStack.push(new byte[]{0});
        }
    }

    /**
     * Convierte un arreglo de bytes a un entero, asumiendo que el arreglo representa un número en formato little-endian.
     * @param bytes el arreglo de bytes a convertir
     * @return el valor entero representado por el arreglo de bytes
     */
    private int bytesToInt(byte[] bytes) {
    if (bytes.length == 0) return 0;
    int result = 0;
    for (int i = 0; i < bytes.length && i < 4; i++) {
        result |= (bytes[i] & 0xFF) << (8 * i);
    }
    return result;
    }
}
