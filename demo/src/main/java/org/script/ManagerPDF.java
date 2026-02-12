package org.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase para leer y ejecutar scripts Bitcoin desde archivos .txt
 */
public class ManagerPDF {
    
    private interpreter interprete;
    
    public ManagerPDF() {
        this.interprete = new interpreter();
    }
    
    /**
     * Lee un archivo .txt y convierte cada línea en una instrucción
     */
    private String[] leerArchivo(File archivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        String resultado = "";
        
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            
            // Ignora líneas vacías y comentarios
            if (linea.isEmpty() || linea.startsWith("#")) {
                continue;
            }
            
            resultado += linea + " ";
        }
        
        br.close();
        
        // Separar por espacios
        return resultado.trim().split("\\s+");
    }
    
    /**
     * Procesa un archivo de script
     */
    public boolean procesarArchivo(String ruta) {
        File archivo = new File(ruta);
        
        // Verificar que existe
        if (!archivo.exists()) {
            System.out.println("Error: El archivo no existe");
            return false;
        }
        
        try {
            System.out.println("Procesando: " + archivo.getName());
            System.out.println();
            
            // Leer script
            String[] script = leerArchivo(archivo);
            
            // Mostrar
            System.out.println("Script cargado:");
            for (int i = 0; i < script.length; i++) {
                System.out.println((i+1) + ". " + script[i]);
            }
            System.out.println();
            
            // Ejecutar - CAMBIO AQUÍ
            boolean resultado = interprete.evaluateScript(script);
            
            // Resultado
            if (resultado) {
                System.out.println("SCRIPT VALIDO");
            } else {
                System.out.println("SCRIPT INVALIDO");
            }
            
            return resultado;
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Esto es solo para crear un archivo de ejemplo, no es parte del codigo principal
     */
    public static void crearEjemplo(String ruta) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(ruta));
            writer.println("# Script de ejemplo");
            writer.println("<hello>");
            writer.println("<hello>");
            writer.println("OP_EQUAL");
            writer.close();
            
            System.out.println("Archivo creado: " + ruta);
            
        } catch (IOException e) {
            System.out.println("Error al crear archivo: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso:");
            System.out.println("  java ManagerPDF <archivo.txt>");
            System.out.println("  java ManagerPDF --crear <archivo.txt>");
            return;
        }
        
        if (args[0].equals("--crear")) {
            crearEjemplo(args[1]);
        } else {
            ManagerPDF manager = new ManagerPDF();
            manager.procesarArchivo(args[0]);
        }
    }
}