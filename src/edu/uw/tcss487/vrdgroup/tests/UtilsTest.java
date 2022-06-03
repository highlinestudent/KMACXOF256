package edu.uw.tcss487.vrdgroup.tests;

import edu.uw.tcss487.vrdgroup.main.SHA3;
import edu.uw.tcss487.vrdgroup.main.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Duy Vu
 * Unit tests for util functions
 */
public class UtilsTest {

    @Test
    void byteArrayToBitArray() {
        Assertions.assertArrayEquals(new byte[] {1, 1, 1, 1, 1, 1, 1, 1}, Utils.byteArrayToBitArray(new byte[] {(byte)0xFF}));
        Assertions.assertArrayEquals(new byte[] {1, 1, 1, 1, 1, 1, 1, 0}, Utils.byteArrayToBitArray(new byte[] {(byte)0xFE}));
        Assertions.assertArrayEquals(new byte[] {1, 1, 1, 1, 0, 0, 0, 0}, Utils.byteArrayToBitArray(new byte[] {(byte)0xF0}));
        Assertions.assertArrayEquals(new byte[] {0, 1, 0, 1, 0, 1, 0, 1}, Utils.byteArrayToBitArray(new byte[] {(byte)0x55}));
        Assertions.assertArrayEquals(new byte[] {1, 0, 1, 0, 1, 0, 1, 0}, Utils.byteArrayToBitArray(new byte[] {(byte)0xAA}));
    }

    @Test
    void hexStringToBitArray() {
        Assertions.assertArrayEquals(new byte[] {1, 1, 1, 1, 1, 1, 1, 1}, Utils.hexStringToBitArray("FF"));
        Assertions.assertArrayEquals(new byte[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}, Utils.hexStringToBitArray("FF FE"));
        Assertions.assertArrayEquals(new byte[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, Utils.hexStringToBitArray("FF FE 00"));
    }
}
