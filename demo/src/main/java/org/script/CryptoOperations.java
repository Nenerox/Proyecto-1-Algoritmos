package org.script;

import java.security.MessageDigest;
import java.util.Arrays;

public class CryptoOperations {
    
    // Para OP_HASH160
    public static byte[] hash160(byte[] data) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash1 = sha256.digest(data);
            byte[] hash2 = sha256.digest(hash1);
            return Arrays.copyOf(hash2, 20);
        } catch (Exception e) {
            throw new RuntimeException("Error hash160");
        }
    }
    
    // Para OP_CHECKSIG
    public static boolean checkSignature(byte[] signature, byte[] publicKey) {
        if (signature == null || publicKey == null) return false;
        if (signature.length < 64 || signature.length > 73) return false;
        if (publicKey.length != 33 && publicKey.length != 65) return false;
        return true;
    }
}