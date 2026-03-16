package org.script;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private Stack<byte[]> scriptStack;
    private Stack<Boolean> stackFlujo;
    private OPcodeOperations operations;
    private Map<String, OPcode> tablaOpCodes;

   /**
    * Constructor del intérprete, inicializa las pilas, la clase de operaciones y registra los OPcodes en un hash map para su fácil acceso.
    */
    public Interpreter() {
        this.scriptStack = new Stack<>();
        this.stackFlujo = new Stack<>();
        this.operations = new OPcodeOperations();
        this.tablaOpCodes = new HashMap<>();
        registrarOPcodes();
    }

    /**
     * Registra los OPcodes en un hash map para su fácil acceso durante la ejecución del script. Cada OPcode se asocia con su método correspondiente en la clase OPcodeOperations.
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
        tablaOpCodes.put("OP_ADD", operations::OP_ADD);
        tablaOpCodes.put("OP_SUB", operations::OP_SUB);
        tablaOpCodes.put("OP_NUMEQUALVERIFY", operations::OP_NUMEQUALVERIFY);
    }

    /**
     * Evalúa un script dado como un array de strings, ejecutando cada instrucción en orden.
     * @param script el script a evaluar, representado como un array de strings donde cada string es una instrucción u operación
     * @return true si el resultado final en la stack es verdadero, false si es falso o si ocurre algún error durante la ejecución
     */
    public boolean evaluateScript(String[] script) {
        try {
            for (String inst : script) {
                if (inst.equals("NL")){
                    System.out.println("Resultado del script: " + resultadoFinal() + "\n");
                    System.out.println("Nueva linea de instrucciones. Limpiando pilas.");
                }   else {
                    System.out.println("Instruccion realizada: " + inst);
                }
                ejecutar(inst);
                System.out.println(scriptStack.trace()+"\n");

            }

        if (scriptStack.isEmpty());
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private boolean resultadoFinal() {
         if (scriptStack.isEmpty()){
            return true;
         } else {
            byte[] top = scriptStack.peek();
            return !Arrays.equals(top, new byte[] {0})
        }
    }

    /**
     * Ejecuta los metodos de Opcodes dependiendo de la instruccion actual
     * @param inst la instrucción a ejecutar, que puede ser un OPcode, un valor a pushear o una instrucción de control de flujo
     */
    private void ejecutar(String inst) {

        if(inst.equals("NL")){
            scriptStack.clear();
            stackFlujo.clear();
            return;
        }

        if (inst.equals("OP_IF") || inst.equals("OP_ELSE")|| inst.equals("OP_ENDIF") || inst.equals("OP_NOTIF")) {
            manejarFlujo(inst);
            return;
        }

        if (!existeIF()) {
            return;
        }

        if (inst.equals("OP_RETURN")) {
            throw new RuntimeException("OP_RETURN ejecutado");
        }

        if (inst.equals("OP_VERIFY")){
            boolean condicion = popBoolean();
            if (!condicion){
                throw new RuntimeException("OP_VERIFY fallido");
            }
            return;
        }

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
     * Verifica si el bloque actual de ejecución está dentro de un OP_IF o OP_NOTIF que se evalúa como verdadero. 
     */
    private boolean existeIF(){
        return stackFlujo.isEmpty() || stackFlujo.peek();
    }
    /**
     * Maneja las instrucciones de control de flujo OP_IF, OP_NOTIF, OP_ELSE y OP_ENDIF. 
     * @param inst la instrucción de control de flujo a manejar (OP_IF, OP_NOTIF, OP_ELSE, OP_ENDIF)
     */
    private void manejarFlujo(String inst){
        if (inst.equals("OP_IF")) {
            boolean condicion = popBoolean();
            stackFlujo.push(condicion);
        } else if (inst.equals("OP_NOTIF")) {
            boolean condicion = popBoolean();
            stackFlujo.push(!condicion);
        } else if (inst.equals("OP_ELSE")) {
            if(stackFlujo.isEmpty()){
                throw new RuntimeException("OP_ELSE sin previo OP_IF");
            }
            boolean actual = stackFlujo.pop();
            stackFlujo.push(!actual);
        } else if(inst.equals("OP_ENDIF")) {
            if(stackFlujo.isEmpty()){
                throw new RuntimeException("OP_ENDIF sin previo OP_IF");
            }
            stackFlujo.pop();
        }
    }

    /**
     * Convierte una cadena hexadecimal a un arreglo de bytes.
     * @param hex la cadena hexadecimal a convertir
     * @return byte[] el arreglo de bytes resultante de la conversión 
     */
    private byte[] hexToBytes(String hex) {
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Devuelve si el top del stack es true si es un valor diferente de cero 
     * Se movio de clase Stack ya que al hacer Stack generic no se puede utilizar al depender de que sean bytes.
     */

    private boolean popBoolean() {
        byte[] top = scriptStack.pop();
        return !Arrays.equals(top, new byte[] {0});
    }

    /**
     * Resetear el stack
     */
    public void reset() {
        scriptStack.clear();
    }
}
