package edu.uw.tcss487.vrdgroup.tests;

import edu.uw.tcss487.vrdgroup.main.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class MainTest {

    @Test
    void right_encode() {
        Assertions.assertArrayEquals(
            //binary: 00000000 1000000
            new byte[]{
                    0,
                    (byte)0x80
            },
            Main.right_encode(new BigInteger("0"))
        );
    }

    @Test
    void left_encode() {
        Assertions.assertArrayEquals(
            //binary: 10000000 0000000
            new byte[]{
                    (byte)0x80,
                    0
            },
            Main.left_encode(new BigInteger("0"))
        );

        Assertions.assertArrayEquals(
            //binary: 10000000 0000000
            new byte[]{
                    (byte)-64,
                    (byte)72,
                    (byte)107,
                    (byte)-31
            },
            Main.left_encode(new BigInteger("1234567"))
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
        Assertions.assertArrayEquals(Main.base256(new BigInteger("1234567"), 3),
                new byte[]{
                        (byte)18,
                        (byte)214,
                        (byte)135
                }
        );
    }
    @Test
    void enc8() {
        Assertions.assertEquals((byte)0, Main.enc8(0));
        Assertions.assertEquals((byte)-128, Main.enc8(1));
        Assertions.assertEquals((byte)64, Main.enc8(2));
        Assertions.assertEquals((byte)72, Main.enc8(18));
        Assertions.assertEquals((byte)107, Main.enc8(214));
        Assertions.assertEquals((byte)225, Main.enc8(135));
    }

    @Test
    void findNForLeftRightEncode() {
        Assertions.assertEquals(1, Main.findNForLeftRightEncode(BigInteger.valueOf(0)));
        Assertions.assertEquals(1, Main.findNForLeftRightEncode(BigInteger.valueOf(1)));
        Assertions.assertEquals(2, Main.findNForLeftRightEncode(BigInteger.valueOf(256)));
        Assertions.assertEquals(3, Main.findNForLeftRightEncode(BigInteger.valueOf(202320)));
    }
}