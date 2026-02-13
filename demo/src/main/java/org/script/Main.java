/**
 * Proyecto 1: Fase 1 - Bitcoin Script Interpreter
 * Integrantes:
 *      - Andrés Pineda          25212
 *      - Alejandro Sagastume    25257
 *      - Jimena Vásquez         25092
 * Clase Main que inicia el programa, lee un archivo de texto con un script Bitcoin y lo evalúa usando la clase ManagerPDF.
 */

package org.script;

public class Main {
    public static void main(String[] args) {
        interpreter interpreter = new interpreter();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  BITCOIN SCRIPT INTERPRETER - Fase 1   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        String[] script = {
            "<0101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101>",
            "<020202020202020202020202020202020202020202020202020202020202020202>",
            "OP_DUP",
            "OP_HASH160",
            "<A9A8D4AE65DE409A1EF6AB6608F0CE3FED019438>",
            "OP_EQUALVERIFY",
            "OP_CHECKSIG"
        };

        boolean resutlado = interpreter.evaluateScript(script);

        System.out.print("resultado: " + resutlado);
    }
}