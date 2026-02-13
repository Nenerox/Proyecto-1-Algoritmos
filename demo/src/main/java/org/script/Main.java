/**
 * Proyecto 1: Fase 1 - Bitcoin Script Interpreter
 * Integrantes:
 *      - Andrés Pineda          25212
 *      - Alejandro Sagastume    25257
 *      - Jimena Vásquez         25092
 * Clase Main que inicia el programa, lee un archivo de texto con un script Bitcoin y lo evalúa usando la clase ManagerPDF.
 */

package org.script;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  BITCOIN SCRIPT INTERPRETER - Fase 1   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Read script from txt.file
        TXTReader reader = new TXTReader();
        String ruta = "script.txt";
        String[] script = reader.leerArchivo(new File(ruta));
        System.out.println("Script cargado:");
        // Print the whole script in one line
        for (String inst : script) {
            System.out.print(inst + " ");
        }
        Interpreter interprete = new Interpreter();
        System.out.println("\n\nEvaluando script...");
        boolean resultado = interprete.evaluateScript(script);
        System.out.println("\nResultado de la evaluación: " + (resultado) + "\n");

    }
}