package org.script;

import java.security.MessageDigest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptoOperations {

    static {
        // Agregar el proveedor de seguridad de Bouncy Castle para usar RIPEMD-160
        java.security.Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Realiza un hash SHA-256 seguido de otro SHA-256 (hash256) sobre los datos proporcionados.
     * @param data los datos a hashear
     * @return byte[] resultado del hash256
     */
    public static byte[] hash256(byte[] data) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash1 = sha256.digest(data);
            byte[] hash2 = sha256.digest(hash1);
            return hash2;
        } catch (Exception e) {
            throw new RuntimeException("Error hash256");
        }
    }
    /**
     * Realiza un hash SHA-256 seguido de un RIPEMD-160 (hash160) sobre los datos proporcionados.
     * @param data los datos a hashear
     * @return byte[] resultado del hash160
     */
    public static byte[] hash160(byte[] data) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD160", "BC");
            byte[] hash1 = sha256.digest(data);
            byte[] hash2 = ripemd160.digest(hash1);
            return hash2;
        } catch (Exception e) {
            throw new RuntimeException("Error hash160");
        }
    }
    /**
     * Verifica una firma digital dada una firma y una clave pública. En esta implementación simplificada, solo se verifica que la firma y la clave pública tengan longitudes válidas.
     * @param signature la firma a verificar
     * @param publicKey la clave pública
     * @return true si la firma es válida, false en caso contrario
     */
    public static boolean checkSignature(byte[] signature, byte[] publicKey) {
        if (signature == null || publicKey == null) return false;
        if (signature.length < 64 || signature.length > 73) return false;
        if (publicKey.length != 33 && publicKey.length != 65) return false;
        return true;
    }
}