package org.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TXTReader {
    /**
     * Lee un archivo de texto línea por línea, ignorando líneas vacías y comentarios (líneas que comienzan con '#'), y devuelve el contenido del archivo como un arreglo de strings, donde cada elemento es una línea del script. Las líneas se concatenan en una sola cadena con " NL " como separador antes de dividirla en el arreglo.
     * @param archivo el archivo de texto a leer
     * @return un arreglo de strings con el contenido del archivo
     * @exception FileNotFoundException si el archivo no se encuentra, con un mensaje de error indicando que el archivo no se pudo encontrar
     * @exception IOException si ocurre un error al leer el archivo, con un mensaje de error indicando que hubo un problema al leer el archivo
     */
    public String[] leerArchivo(File archivo) {
        String resultado = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                continue;
                }
            resultado += linea + " NL ";        
            }
            br.close(); 
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Error al leer el archivo: " + fileNotFoundException.getMessage());
        } catch (IOException ioException) {
            System.out.println("Error al leer el archivo: " + ioException.getMessage());
        }
        // Retornar el archivo como un arreglo de strings, cada elemento es una línea del script
        return resultado.trim().split("\\s+");
    }
}