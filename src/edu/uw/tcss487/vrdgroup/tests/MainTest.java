package edu.uw.tcss487.vrdgroup.tests;

import edu.uw.tcss487.vrdgroup.main.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void right_encode() {
        Assertions.assertArrayEquals(Main.right_encode(0),
                //binary: 00000000 1000000
                new byte[]{
                        0,
                        (byte)0x80
                });
    }

    @Test
    void left_encode() {
        Assertions.assertArrayEquals(Main.left_encode(0),
                //binary: 10000000 0000000
                new byte[]{
                        (byte)0x80,
                        0
                });
    }

    @Test
    void encode_string() {
        Assertions.assertArrayEquals(Main.encode_string("".getBytes()),
                //binary: 10000000 0000000
                new byte[]{
                        (byte)0x80,
                        0
                });
    }

    @Test
    void bytepad() {
    }
}