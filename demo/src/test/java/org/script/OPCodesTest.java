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
    public void testOP_0() {
        stack.push(new byte[]{0x01});
        operations.OP_0(stack);
        assertArrayEquals(new byte[]{}, stack.pop());
    }

    @Test
    public void testOP_1() {
        stack.push(new byte[]{0x01});
        operations.OP_1(stack);
        assertArrayEquals(new byte[]{0x01}, stack.pop());
    }
    
}
