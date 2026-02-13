package org.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TXTReader {
    // Atributo
    private interpreter interprete;
    
    // Constructor
    public TXTReader() {
        this.interprete = new interpreter();
    }
    
    private String[] leerArchivo(File archivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        String resultado = "";
        
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) {
                continue;
            }
            resultado += linea + " ";
        }
        br.close();

        return resultado.trim().split("\\s+");
    }
    
    public boolean procesarArchivo(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println("Error: El archivo no existe");
            return false;
        }
        try {
            System.out.println("Procesando: " + archivo.getName());
            System.out.println();
            String[] script = leerArchivo(archivo);
            System.out.println("Script cargado:");

            for (int i = 0; i < script.length; i++) {
                System.out.println((i+1) + ". " + script[i]);
            }
            System.out.println();
            boolean resultado = interprete.evaluateScript(script);            
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
}