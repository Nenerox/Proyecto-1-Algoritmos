package org.script;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.script.Interpreter;
import org.script.OPcodeOperations;
import org.script.OPcode;


public class OPCodesTest {
    @Test
    public void testOP_0() {
        OPcodeOperations operations = new OPcodeOperations();
        byte[] result = operations.OP_0();
        assertArrayEquals(new byte[0], result, "OP_0 should push an empty byte array onto the stack");
    }
    @Test
    public void testOP_1() {
        OPcodeOperations operations = new OPcodeOperations();
        byte[] result = operations.OP_1();
        assertArrayEquals(new byte[]{0x01}, result, "OP_1 should push a byte array with value 1 onto the stack");
    }
    @Test
    public void testOP_DUP() {
        OPcodeOperations operations = new OPcodeOperations();
        byte[] input = new byte[]{0x01, 0x02};
        byte[] result = operations.OP_DUP(input);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x01, 0x02}, result, "OP_DUP should duplicate the top item on the stack");
    }
    @Test
    public void testOP_DROP() {
        OPcodeOperations operations = new OPcodeOperations();
        byte[] input = new byte[]{0x01, 0x02};
        byte[] result = operations.OP_DROP(input);
        assertArrayEquals(new byte[0], result, "OP_DROP should remove the top item from the stack");
    }
    @Test
    public void testOP_EQUAL() {
        OPcodeOperations operations = new OPcodeOperations();
        byte[] input1 = new byte[]{0x01, 0x02};
        byte[] input2 = new byte[]{0x01, 0x02};
        boolean result = operations.OP_EQUAL(input1, input2);
        assertTrue(result, "OP_EQUAL should return true for equal inputs");
    }
}
