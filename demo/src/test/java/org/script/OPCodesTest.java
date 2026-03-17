package org.script;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;


public class OPCodesTest {
    private OPcodeOperations operations;
    private Stack<byte[]> stack;

    @Before
    public void setUp() {
        operations = new OPcodeOperations();
        stack = new Stack<>();
    }

    @Test
    public void testOP_0_success() {
        stack.push(new byte[]{0x01});
        operations.OP_0(stack);
        assertArrayEquals(new byte[]{0x00}, stack.pop());
    }

    @Test
    public void testOP_1_success() {
        stack.push(new byte[]{0x01});
        operations.OP_1(stack);
        assertArrayEquals(new byte[]{0x01}, stack.pop());
    }
    
    @Test
    public void testOP_2_16_valid() {
        for (byte i = 2; i <= 16; i++) {
            stack.push(new byte[]{0x01});
            operations.OP_2_16(stack, i);
            assertArrayEquals(new byte[]{i}, stack.pop());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOP_2_16_invalid() { 
        operations.OP_2_16(stack, (byte) 1);
    }

    @Test
    public void testOP_PUSHDATA() {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        operations.OP_PUSHDATA(stack, data);
        byte[] top = stack.pop();
        assertArrayEquals("OP_PUSHDATA DEBE PUSHEAR LOS DATOS CORRECTOS", data, top);
    }

    @Test
    public void testOP_DUP() {
        stack.push(new byte[]{0x01, 0x02, 0x03});
        operations.OP_DUP(stack);
        byte[] top1 = stack.pop();
        byte[] top2 = stack.pop();
        assertArrayEquals("OP_DUP DEBE DUPLICAR EL TOP DEL STACK", new byte[]{0x01, 0x02, 0x03}, top1);
        assertArrayEquals("EL SEGUNDO DEBE SER IGUAL AL PRIMERO", new byte[]{0x01, 0x02, 0x03}, top2);
    }
    
    @Test
    public void testOP_HASH160() {
        stack.push(new byte[]{0x01, 0x02, 0x03});
        operations.OP_HASH160(stack);
        byte[] top = stack.pop();
        assertNotNull("OP_HASH160 DEBE PUSHEAR UN HASH", top);
        assertEquals("EL HASH DEBE TENER LONGITUD 20", 20, top.length);
    }

    @Test
    public void testOP_EQUAL() {
        stack.push(new byte[]{0x01, 0x02, 0x03});
        stack.push(new byte[]{0x01, 0x02, 0x03});
        operations.OP_EQUAL(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_EQUAL DEBE PUSHEAR 1 SI LOS TOP SON IGUALES", new byte[]{0x01}, result);

        stack.push(new byte[]{0x01, 0x02, 0x03});
        stack.push(new byte[]{0x04, 0x05, 0x06});
        operations.OP_EQUAL(stack);
        result = stack.pop();
        assertArrayEquals("OP_EQUAL DEBE PUSHEAR 0 SI LOS TOP SON DIFERENTES", new byte[]{0x00}, result);
    }

    @Test
    public void testOP_EQUALVERIFY() {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        stack.push(data);
        operations.OP_EQUALVERIFY(stack);
        assertTrue("OP_EQUALVERIFY DEBE DEJAR EL STACK VACÍO SI LOS TOP SON IGUALES", stack.isEmpty());

        stack.push(new byte[]{0x00});
        try {
            operations.OP_EQUALVERIFY(stack);
            fail("OP_EQUALVERIFY DEBE LANZAR UNA EXCEPCIÓN SI LOS TOP SON DIFERENTES");
        } catch (RuntimeException e) {
            assertEquals("OP_EQUALVERIFY DEBE LANZAR UNA EXCEPCIÓN CON EL MENSAJE 'Error en OP_EQUALVERIFY: el valor es falso'", "Error en OP_EQUALVERIFY: el valor es falso", e.getMessage());
        }
    }

    @Test
    public void testOP_CHECKSIG() {
        byte[] signature = new byte[64];
        byte[] publicKey = new byte[33];
        stack.push(signature);
        stack.push(publicKey);
        operations.OP_CHECKSIG(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_CHECKSIG DEBE PUSHEAR 1 SI LA FIRMA ES VÁLIDA", new byte[]{0x01}, result);
    }

    @Test
    public void testOP_CHECKSIG_invalid() {
        byte[] invalidSignature = new byte[10]; // Firma inválida (longitud incorrecta)
        byte[] publicKey = new byte[33];
        stack.push(invalidSignature);
        stack.push(publicKey);
        operations.OP_CHECKSIG(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_CHECKSIG DEBE PUSHEAR 0 SI LA FIRMA ES INVÁLIDA", new byte[]{0x00}, result);
    }
}
