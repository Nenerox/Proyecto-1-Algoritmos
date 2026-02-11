import java.util.Arrays;

public class OPcodeOperations {
    /*Todo:
    -Fase 1
    OP_HASH160
    OP_CHECKSIG
    */
    

    //0 y 1 son los valores booleanos en Bitcoin Script de falso y verdadero.
    public void OP_0(Stack ScriptStack) {
        ScriptStack.push(new byte[] {0});
    }
    public void OP_1(Stack ScriptStack) {
        ScriptStack.push(new byte[] {1});
    }

    public void OP_2_16(Stack ScriptStack, byte n) {
        if (n < 2 || n > 16) {
            throw new IllegalArgumentException("numero debe estar entre 2 y 16");
        }
        ScriptStack.push(new byte[] {n});
    }

    
    public void OP_PUSHDATA(Stack ScriptStack, byte[] data) {
        ScriptStack.push(data);
    }

    public void OP_DUP(Stack ScriptStack) {
        byte[] top = ScriptStack.peek();
        ScriptStack.push(top);
    }
    public void OP_DROP(Stack ScriptStack) {
        ScriptStack.pop();
    }
    public void OP_EQUAL(Stack ScriptStack) {
        byte[] a = ScriptStack.pop();
        byte[] b = ScriptStack.pop();

        if (Arrays.equals(a, b)) {
            ScriptStack.push(new byte[] {1});
        } else {
            ScriptStack.push(new byte[] {0});
        }
    }

    public void OP_EQUALVERIFY(Stack ScriptStack) {
        OP_EQUAL(ScriptStack);
        if (!ScriptStack.popBoolean()) {
            throw new RuntimeException("OP_EQUALVERIFY failed");
        }
    }
    



    /*Todo:
    -Fase 2
    OP_SWAP
    OP_OVER
    OP_NOT
    OP_BOOLAND
    OP_BOOLOR
    OP_ADD
    OP_SUB
    OP_NUMEQUALVERIFY
    OP_LESSTHAN
    OP_GREATERTHAN
    OP_LESSTHANOREQUAL
    OP_GREATERTHANOREQUAL
    */     
}