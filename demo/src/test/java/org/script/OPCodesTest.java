package org.script;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
        byte[] data = new byte[]{0x01};
        byte[] dataCopy = new byte[]{0x01};
        stack.push(data);
        stack.push(dataCopy);
        operations.OP_EQUALVERIFY(stack);
        assertTrue("OP_EQUALVERIFY DEBE DEJAR EL STACK VACÍO SI LOS TOP SON IGUALES", stack.isEmpty());
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
    @Test
    public void testOP_HASH160_consistency() {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        stack.push(data);
        operations.OP_HASH160(stack);
        byte[] hash1 = stack.pop();

        stack.push(data);
        operations.OP_HASH160(stack);
        byte[] hash2 = stack.pop();

        assertArrayEquals("OP_HASH160 DEBE PRODUCIR EL MISMO HASH PARA LOS MISMOS DATOS", hash1, hash2);
    }
    @Test
    public void testOP_SHA256_consistency() {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        stack.push(data);
        operations.OP_SHA256(stack);
        byte[] hash1 = stack.pop();

        stack.push(data);
        operations.OP_SHA256(stack);
        byte[] hash2 = stack.pop();

        assertArrayEquals("OP_SHA256 DEBE PRODUCIR EL MISMO HASH PARA LOS MISMOS DATOS", hash1, hash2);
    }
    @Test
    public void test_OP_HASH256_consistency() {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        stack.push(data);
        operations.OP_HASH256(stack);
        byte[] hash1 = stack.pop();

        stack.push(data);
        operations.OP_HASH256(stack);
        byte[] hash2 = stack.pop();

        assertArrayEquals("OP_HASH256 DEBE PRODUCIR EL MISMO HASH PARA LOS MISMOS DATOS", hash1, hash2);
    }
    @Test
    public void testOP_SWAP() {
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x02});
        operations.OP_SWAP(stack);
        byte[] top1 = stack.pop();
        byte[] top2 = stack.pop();
        assertArrayEquals("OP_SWAP DEBE INTERCAMBIAR LOS DOS TOP DEL STACK", new byte[]{0x01}, top1);
        assertArrayEquals("EL SEGUNDO DEBE SER EL PRIMERO ANTES DEL SWAP", new byte[]{0x02}, top2);
    }
    @Test
    public void testOP_DROP() {
        stack.push(new byte[]{0x01});
        operations.OP_DROP(stack);
        assertTrue("OP_DROP DEBE ELIMINAR EL TOP DEL STACK", stack.isEmpty());
    }
    @Test
    public void testOP_OVER() {
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x02});
        operations.OP_OVER(stack);
        byte[] top1 = stack.pop();
        byte[] top2 = stack.pop();
        byte[] top3 = stack.pop();
        assertArrayEquals("OP_OVER DEBE PUSHEAR UNA COPIA DEL SEGUNDO ELEMENTO", new byte[]{0x01}, top1);
        assertArrayEquals("EL SEGUNDO DEBE SER EL PRIMERO ANTES DEL OVER", new byte[]{0x02}, top2);
        assertArrayEquals("EL TERCERO DEBE SER EL SEGUNDO ANTES DEL OVER", new byte[]{0x01}, top3);
    }
    @Test
    public void testOP_NOT() {
        stack.push(new byte[]{0x01});
        operations.OP_NOT(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_NOT DEBE PUSHEAR 0 SI EL TOP ES VERDADERO", new byte[]{0x00}, result);
        stack.push(new byte[]{0x00});
        operations.OP_NOT(stack);
        result = stack.pop();
        assertArrayEquals("OP_NOT DEBE PUSHEAR 1 SI EL TOP ES FALSO", new byte[]{0x01}, result);
    }
    @Test
    public void testOP_BOOLAND() {
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x01});
        operations.OP_BOOLAND(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_BOOLAND DEBE PUSHEAR 1 SI AMBOS TOP SON VERDADEROS", new byte[]{0x01}, result);
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x00});
        operations.OP_BOOLAND(stack);
        result = stack.pop();
        assertArrayEquals("OP_BOOLAND DEBE PUSHEAR 0 SI UNO DE LOS TOP ES FALSO", new byte[]{0x00}, result);
    }
    @Test
    public void testOP_BOOLOR() {
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x00});
        operations.OP_BOOLOR(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_BOOLOR DEBE PUSHEAR 1 SI UNO DE LOS TOP ES VERDADERO", new byte[]{0x01}, result);
        stack.push(new byte[]{0x00});
        stack.push(new byte[]{0x00});
        operations.OP_BOOLOR(stack);
        result = stack.pop();
        assertArrayEquals("OP_BOOLOR DEBE PUSHEAR 0 SI AMBOS TOP SON FALSOS", new byte[]{0x00}, result);
    }
    @Test
    public void testOP_ADD() {
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x02});
        operations.OP_ADD(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_ADD DEBE PUSHEAR LA SUMA DE LOS DOS TOP", new byte[]{0x03}, result);
    }
    @Test
    public void testOP_SUB() {
        stack.push(new byte[]{0x03});
        stack.push(new byte[]{0x01});
        operations.OP_SUB(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_SUB DEBE PUSHEAR LA RESTA DEL PRIMER TOP MENOS EL SEGUNDO", new byte[]{0x02}, result);
    }
    @Test
    public void testOP_NUMEQUALVERIFY() {
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x01});
        operations.OP_NUMEQUALVERIFY(stack);
        assertTrue("OP_NUMEQUALVERIFY DEBE DEJAR EL STACK VACÍO SI LOS TOP SON NUMÉRICAMENTE IGUALES", stack.isEmpty());
    }
    @Test
    public void testOP_LESSTHANOREQUAL() {
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x02});
        operations.OP_LESSTHANOREQUAL(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_LESSTHANOREQUAL DEBE PUSHEAR 1 SI EL PRIMER TOP ES MENOR O IGUAL QUE EL SEGUNDO", new byte[]{0x01}, result);
        stack.push(new byte[]{0x02});
        stack.push(new byte[]{0x01});
        operations.OP_LESSTHANOREQUAL(stack);
        result = stack.pop();
        assertArrayEquals("OP_LESSTHANOREQUAL DEBE PUSHEAR 0 SI EL PRIMER TOP NO ES MENOR O IGUAL QUE EL SEGUNDO", new byte[]{0x00}, result);
    }
    @Test
    public void testOP_LESSTHAN() {
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x02});
        operations.OP_LESSTHAN(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_LESSTHAN DEBE PUSHEAR 1 SI EL PRIMER TOP ES MENOR QUE EL SEGUNDO", new byte[]{0x01}, result);
        stack.push(new byte[]{0x02});
        stack.push(new byte[]{0x01});
        operations.OP_LESSTHAN(stack);
        result = stack.pop();
        assertArrayEquals("OP_LESSTHAN DEBE PUSHEAR 0 SI EL PRIMER TOP NO ES MENOR QUE EL SEGUNDO", new byte[]{0x00}, result);
    }
    @Test
    public void testOP_GREATERTHAN() {
        stack.push(new byte[]{0x02});
        stack.push(new byte[]{0x01});
        operations.OP_GREATERTHAN(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_GREATERTHAN DEBE PUSHEAR 1 SI EL PRIMER TOP ES MAYOR QUE EL SEGUNDO", new byte[]{0x01}, result);
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x02});
        operations.OP_GREATERTHAN(stack);
        result = stack.pop();
        assertArrayEquals("OP_GREATERTHAN DEBE PUSHEAR 0 SI EL PRIMER TOP NO ES MAYOR QUE EL SEGUNDO", new byte[]{0x00}, result);
    }
    @Test
    public void testOP_GREATERTHANOREQUAL() {
        stack.push(new byte[]{0x02});
        stack.push(new byte[]{0x01});
        operations.OP_GREATERTHANOREQUAL(stack);
        byte[] result = stack.pop();
        assertArrayEquals("OP_GREATERTHANOREQUAL DEBE PUSHEAR 1 SI EL PRIMER TOP ES MAYOR O IGUAL QUE EL SEGUNDO", new byte[]{0x01}, result);
        stack.push(new byte[]{0x01});
        stack.push(new byte[]{0x02});
        operations.OP_GREATERTHANOREQUAL(stack);
        result = stack.pop();
        assertArrayEquals("OP_GREATERTHANOREQUAL DEBE PUSHEAR 0 SI EL PRIMER TOP NO ES MAYOR O IGUAL QUE EL SEGUNDO", new byte[]{0x00}, result);
    }
}
