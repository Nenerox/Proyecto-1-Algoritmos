package org.script;

import java.util.Arrays;

public class OPcodeOperations {
    
    // 0 y 1 son los valores booleanos en Bitcoin Script de falso y verdadero.
    public void OP_0(Stack<byte[]> ScriptStack) {
        ScriptStack.push(new byte[] {0});
    }
    
    public void OP_1(Stack<byte[]> ScriptStack) {
        ScriptStack.push(new byte[] {1});
    }

    public void OP_2_16(Stack<byte[]> ScriptStack, byte n) {
        if (n < 2 || n > 16) {
            throw new IllegalArgumentException("numero debe estar entre 2 y 16");
        }
        ScriptStack.push(new byte[] {n});
    }

    public void OP_PUSHDATA(Stack<byte[]> ScriptStack, byte[] data) {
        ScriptStack.push(data);
    }

    public void OP_DUP(Stack<byte[]> ScriptStack) {
        byte[] top = ScriptStack.peek();
        ScriptStack.push(top);
    }
    
    public void OP_DROP(Stack<byte[]> ScriptStack) {
        ScriptStack.pop();
    }
    
    public void OP_EQUAL(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();

        if (Arrays.equals(a, b)) {
            ScriptStack.push(new byte[] {1});
        } else {
            ScriptStack.push(new byte[] {0});
        }
    }

    public void OP_EQUALVERIFY(Stack<byte[]> ScriptStack) {
        OP_EQUAL(ScriptStack);
        if (!popBoolean(ScriptStack)) {
            throw new RuntimeException("OP_EQUALVERIFY failed");
        }
    }

    private boolean popBoolean(Stack<byte[]> stack) {
    byte[] top = stack.pop();
    return !Arrays.equals(top, new byte[]{0});
    }

    public void OP_HASH160(Stack<byte[]> ScriptStack) {
        byte[] data = ScriptStack.pop();
        byte[] hash = CryptoOperations.hash160(data);
        ScriptStack.push(hash);
    }

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

    /*
     * TODO - Fase 2:
     * OP_SWAP, OP_OVER, OP_NOT, OP_BOOLAND, OP_BOOLOR
     * OP_ADD, OP_SUB, OP_NUMEQUALVERIFY
     * OP_LESSTHAN, OP_GREATERTHAN
     * OP_LESSTHANOREQUAL, OP_GREATERTHANOREQUAL
     */
    
    public void OP_SWAP(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();
        ScriptStack.push(a);
        ScriptStack.push(b);
    }

    public void OP_OVER(Stack<byte[]> ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.peek();
        ScriptStack.push(a);
        ScriptStack.push(b);
    }

    public void OP_NOT(Stack<byte[]> ScriptStack) {
        byte[] value = ScriptStack.pop();
        if (value.length == 0 || (value.length == 1 && value[0] == 0)) {
            ScriptStack.push(new byte[] {1});
        } else {
            ScriptStack.push(new byte[] {0});
        }
    }
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
    public void OP_NUMEQUALVERIFY(Stack<byte[]> ScriptStack) {
        // OP_NUMEQUAL con OP_VERIFY
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
}
