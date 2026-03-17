package org.script;

import static org.junit.Assert.*;

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
    
}
