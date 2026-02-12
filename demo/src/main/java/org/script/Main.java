package org.script;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  BITCOIN SCRIPT INTERPRETER - Fase 1  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        String archivo = "script.txt";
        
        if (args.length > 0) {
            archivo = args[0];
        }
        
        ManagerPDF manager = new ManagerPDF();
        manager.procesarArchivo(archivo);
    }
}