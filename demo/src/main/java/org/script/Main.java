/**
 * Proyecto 1: Fase 2 - Bitcoin Script Interpreter
 * @author Andrés Pineda          25212
 * @author Alejandro Sagastume    25257
 * @author Jimena Vásquez         25092
 * @file Clase Main que inicia el programa, lee un archivo de texto con un script Bitcoin y lo evalúa usando la clase ManagerPDF.
 * @version 2.0
 */

package org.script;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  BITCOIN SCRIPT INTERPRETER - Fase 2   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Read script from txt.file
        TXTReader reader = new TXTReader();
        String ruta = "src/main/java/org/script/Operaciones.txt"; 
        String[] script = reader.leerArchivo(new File(ruta));
        System.out.println("Script cargado:");
        // Print the whole script in one line
        for (String inst : script) {
            System.out.print(inst + " ");
        }
        Interpreter interprete = new Interpreter();
        System.out.println("\n\nEvaluando script...");
        interprete.evaluateScript(script);
    }
}