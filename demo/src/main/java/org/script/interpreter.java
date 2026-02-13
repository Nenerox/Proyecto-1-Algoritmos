package org.script;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private Stack scriptStack;
    private OPcodeOperations operations;
    private Map<String, OPcode> tablaOpCodes;

    /*
    Crea y asigna los objetos para guardar y llamar los diferentes metodos necesarios
    Llama al metodo para asignar los OPcodes
     */
    public Interpreter() {
        this.scriptStack = new Stack();
        this.operations = new OPcodeOperations();
        this.tablaOpCodes = new HashMap<>();
        registrarOPcodes();
    }

    /*
    Se registran todos los metodos de OPcodes en un hash map para poder ser llamados de manera sencilla
    */
    private void registrarOPcodes(){
        tablaOpCodes.put("OP_0", operations::OP_0);
        tablaOpCodes.put("OP_1", operations::OP_1);
        tablaOpCodes.put("OP_DUP", operations::OP_DUP);
        tablaOpCodes.put("OP_DROP",operations::OP_DROP);
        tablaOpCodes.put("OP_EQUAL", operations::OP_EQUAL);
        tablaOpCodes.put("OP_EQUALVERIFY", operations::OP_EQUALVERIFY);
        tablaOpCodes.put("OP_CHECKSIG", operations::OP_CHECKSIG);
        tablaOpCodes.put("OP_HASH160", operations::OP_HASH160);
    }

    /**
     * Evalúa un script completo
     */
    public boolean evaluateScript(String[] script) {
        try {
            for (String inst : script) {
                ejecutar(inst);
            }
            if (scriptStack.isEmpty()) return false;
            return scriptStack.popBoolean();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /*
     Ejecuta los metodos de Opcodes dependiendo de la instruccion actual
     */
    private void ejecutar(String inst) {
        //Se maneja el ingreso de OP_2 a 16 y pushdata individualmente por su caso especial de datos
        if (inst.startsWith("OP_")) {
            try {
                int value = Integer.parseInt(inst.substring(3));
                if (value >= 2 && value <=16) {
                    operations.OP_2_16(scriptStack, (byte) value);
                    return;
                }
            } catch (NumberFormatException  ignored) {
            }
        }

        if (inst.startsWith("<") && inst.endsWith(">")) {
            String data = inst.substring(1, inst.length() - 1);
            operations.OP_PUSHDATA(scriptStack, hexToBytes(data));
            return;
        }

        OPcode OP = tablaOpCodes.get(inst);

        if (OP != null){
            OP.execute(scriptStack);
            return;
        }
        throw new IllegalArgumentException("Instrucción desconocida: " + inst);
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
        scriptStack.clear();
    }
}