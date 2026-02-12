package org.script;

public class interpreter {
    private Stack Scriptstack;
    private OPcodeOperations operations;

    public interpreter() {
        this.Scriptstack = new Stack();
        this.operations = new OPcodeOperations();
    }

    /**
     * Evalúa un script completo
     */
    public boolean evaluateScript(String[] script) {
        try {
            for (String inst : script) {
                ejecutar(inst);
            }
            if (Scriptstack.isEmpty()) return false;
            return Scriptstack.popBoolean();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ejecuta una instrucción individual
     */
    private void ejecutar(String inst) {
        if (inst.equals("OP_0")) operations.OP_0(Scriptstack);
        else if (inst.equals("OP_1")) operations.OP_1(Scriptstack);
        else if (inst.equals("OP_DUP")) operations.OP_DUP(Scriptstack);
        else if (inst.equals("OP_DROP")) operations.OP_DROP(Scriptstack);
        else if (inst.equals("OP_EQUAL")) operations.OP_EQUAL(Scriptstack);
        else if (inst.equals("OP_EQUALVERIFY")) operations.OP_EQUALVERIFY(Scriptstack);
        else if (inst.equals("OP_HASH160")) operations.OP_HASH160(Scriptstack);
        else if (inst.equals("OP_CHECKSIG")) operations.OP_CHECKSIG(Scriptstack);
        else if (inst.startsWith("<") && inst.endsWith(">")) {
            String data = inst.substring(1, inst.length() - 1);
            operations.OP_PUSHDATA(Scriptstack, hexToBytes(data));
        }
        else throw new IllegalArgumentException("Instrucción desconocida: " + inst);
    }

    /**
     * Convierte hexadecimal a bytes
     */
    private byte[] hexToBytes(String hex) {
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    public void reset() {
        Scriptstack.clear();
    }
}