package edu.uw.tcss487.vrdgroup.tests;

import edu.uw.tcss487.vrdgroup.main.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class MainTest {

    @Test
    void right_encode() {
        Assertions.assertArrayEquals(Main.right_encode(new BigInteger("0")),
            //binary: 00000000 1000000
            new byte[]{
                    0,
                    (byte)0x80
            }
        );
    }

    @Test
    void left_encode() {
        Assertions.assertArrayEquals(Main.left_encode(new BigInteger("0")),
            //binary: 10000000 0000000
            new byte[]{
                    (byte)0x80,
                    0
            }
        );
    }

    @Test
    void encode_string() {
        Assertions.assertArrayEquals(Main.encode_string("".getBytes()),
            //binary: 10000000 0000000
            new byte[]{
                (byte)0x80,
                0
            }
        );
    }

    @Test
    void bytepad() {
        throw new IllegalArgumentException();
    }

    @Test
    void base256() {
        Assertions.assertArrayEquals(Main.base256(new BigInteger("0"), 1),
            new byte[]{
               0
            }
        );
        Assertions.assertArrayEquals(Main.base256(new BigInteger("1"), 1),
            new byte[]{
                1
            }
        );
        Assertions.assertArrayEquals(Main.base256(new BigInteger("255"), 1),
            new byte[]{
                (byte)255
            }
        );
        Assertions.assertArrayEquals(Main.base256(new BigInteger("256"), 2),
            new byte[]{
                (byte)1,
                    (byte)0
            }
        );
    }
    @Test
    void enc8() {
        Assertions.assertEquals((byte)0, Main.enc8(0));
        Assertions.assertEquals((byte)-128, Main.enc8(1));
        Assertions.assertEquals((byte)64, Main.enc8(2));
    }
}