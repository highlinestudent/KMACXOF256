package edu.uw.tcss487.vrdgroup.tests;

import edu.uw.tcss487.vrdgroup.main.Keccak1600;
import edu.uw.tcss487.vrdgroup.main.SHA3;
import edu.uw.tcss487.vrdgroup.main.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class Keccak1600Test {
    @Test
    void theta() {
        String input = "1F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00";
        String output = "1E 00 00 00 00 00 00 00 1F 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 " +
                "3E 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 " +
                "1F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 80 " +
                "00 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 00 " +
                "01 00 00 00 00 00 00 00 1F 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 " +
                "3E 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 " +
                "1F 00 00 00 00 00 00 80 00 00 00 00 00 00 00 80 " +
                "00 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 00 " +
                "01 00 00 00 00 00 00 00 1F 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 " +
                "3E 00 00 00 00 00 00 00";

        byte[] bitsInput = Utils.hexStringToBitArray(input);
        byte[] bitsOutput = Utils.hexStringToBitArray(output);

        Utils.printBits(bitsOutput, "Expect Output: ");
        Utils.printBits(Keccak1600.stateArraysToBitArray(Keccak1600.theta(Keccak1600.bitArrayToStateArrays(bitsInput))), "Actual Output: ");

        Assertions.assertArrayEquals(bitsOutput, Keccak1600.stateArraysToBitArray(Keccak1600.theta(Keccak1600.bitArrayToStateArrays(bitsInput))));
    }

    @Test
    void roh() {
        String input = "1E 00 00 00 00 00 00 00 1F 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 " +
                "3E 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 " +
                "1F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 80 " +
                "00 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 00 " +
                "01 00 00 00 00 00 00 00 1F 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 " +
                "3E 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 " +
                "1F 00 00 00 00 00 00 80 00 00 00 00 00 00 00 80 " +
                "00 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 00 " +
                "01 00 00 00 00 00 00 00 1F 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 " +
                "3E 00 00 00 00 00 00 00";
        String output = "1E 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 20 00 00 00 00 00 00 00 00 " +
                "00 00 00 F0 01 00 00 00 00 00 00 00 10 00 00 00 " +
                "00 00 00 00 00 F0 01 00 20 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 E0 03 00 00 00 00 " +
                "08 00 00 00 00 00 00 00 00 7C 00 00 00 00 00 00 " +
                "00 00 00 00 00 04 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 1F 00 00 00 00 00 00 00 02 00 00 " +
                "00 00 00 00 00 F0 03 00 00 40 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 7C 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 10 00 00 00 00 00 00 00 00 " +
                "00 80 0F 00 00 00 00 00";

        byte[] bitsInput = Utils.hexStringToBitArray(input);
        byte[] bitsOutput = Utils.hexStringToBitArray(output);

        Utils.printBits(bitsOutput, "Expect Output: ");
        Utils.printBits(Keccak1600.stateArraysToBitArray(Keccak1600.roh(Keccak1600.bitArrayToStateArrays(bitsInput))), "Actual Output: ");

        Assertions.assertArrayEquals(bitsOutput, Keccak1600.stateArraysToBitArray(Keccak1600.roh(Keccak1600.bitArrayToStateArrays(bitsInput))));
    }

    @Test
    void pi() {
        String input = "1E 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 20 00 00 00 00 00 00 00 00 " +
                "00 00 00 F0 01 00 00 00 00 00 00 00 10 00 00 00 " +
                "00 00 00 00 00 F0 01 00 20 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 00 E0 03 00 00 00 00 " +
                "08 00 00 00 00 00 00 00 00 7C 00 00 00 00 00 00 " +
                "00 00 00 00 00 04 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 1F 00 00 00 00 00 00 00 02 00 00 " +
                "00 00 00 00 00 F0 03 00 00 40 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 7C 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 10 00 00 00 00 00 00 00 00 " +
                "00 80 0F 00 00 00 00 00";
        String output = "1E 00 00 00 00 00 00 00 00 00 00 00 00 F0 01 00 " +
                "00 00 00 00 00 04 00 00 00 00 00 00 00 00 00 00 " +
                "00 80 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 E0 03 00 00 00 00 08 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 F0 03 00 00 00 00 00 00 00 00 10 " +
                "3E 00 00 00 00 00 00 00 20 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 00 00 00 F0 01 00 00 00 " +
                "00 00 00 00 10 00 00 00 00 7C 00 00 00 00 00 00 " +
                "00 40 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 20 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 1F 00 00 00 00 00 00 00 02 00 00 " +
                "7C 00 00 00 00 00 00 00";

        byte[] bitsInput = Utils.hexStringToBitArray(input);
        byte[] bitsOutput = Utils.hexStringToBitArray(output);

        Utils.printBits(bitsOutput, "Expect Output: ");
        Utils.printBits(Keccak1600.stateArraysToBitArray(Keccak1600.pi(Keccak1600.bitArrayToStateArrays(bitsInput))), "Actual Output: ");

        Assertions.assertArrayEquals(bitsOutput, Keccak1600.stateArraysToBitArray(Keccak1600.pi(Keccak1600.bitArrayToStateArrays(bitsInput))));
    }

    @Test
    void chi() {
        String input = "1E 00 00 00 00 00 00 00 00 00 00 00 00 F0 01 00 " +
                "00 00 00 00 00 04 00 00 00 00 00 00 00 00 00 00 " +
                "00 80 0F 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 E0 03 00 00 00 00 08 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 F0 03 00 00 00 00 00 00 00 00 10 " +
                "3E 00 00 00 00 00 00 00 20 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 00 00 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 00 00 00 F0 01 00 00 00 " +
                "00 00 00 00 10 00 00 00 00 7C 00 00 00 00 00 00 " +
                "00 40 00 00 00 00 00 00 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 00 00 20 00 00 00 00 00 00 00 00 " +
                "00 00 00 00 00 1F 00 00 00 00 00 00 00 02 00 00 " +
                "7C 00 00 00 00 00 00 00";
        String output = "1E 00 00 00 00 04 00 00 00 00 00 00 00 F0 01 00 " +
                "00 80 0F 00 00 04 00 00 1E 00 00 00 00 00 00 00 " +
                "00 80 0F 00 00 F0 01 00 08 00 00 00 00 00 00 00 " +
                "00 00 E0 03 00 F0 03 00 08 00 00 00 00 00 00 10 " +
                "00 00 00 00 00 F0 03 00 00 00 E0 03 00 00 00 10 " +
                "3E 00 00 00 00 00 00 00 20 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 3E 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 00 7C 00 F0 01 00 00 00 " +
                "00 00 00 00 10 00 00 00 00 7C 00 00 00 00 00 00 " +
                "00 40 00 F0 01 00 00 00 00 00 00 00 10 00 00 00 " +
                "00 00 00 00 00 1F 00 20 00 00 00 00 00 00 00 00 " +
                "7C 00 00 00 00 1F 00 00 00 00 00 00 00 02 00 20 " +
                "7C 00 00 00 00 00 00 00";

        byte[] bitsInput = Utils.hexStringToBitArray(input);
        byte[] bitsOutput = Utils.hexStringToBitArray(output);

        Utils.printBits(bitsOutput, "Expect Output: ");
        Utils.printBits(Keccak1600.stateArraysToBitArray(Keccak1600.chi(Keccak1600.bitArrayToStateArrays(bitsInput))), "Actual Output: ");

        Assertions.assertArrayEquals(bitsOutput, Keccak1600.stateArraysToBitArray(Keccak1600.chi(Keccak1600.bitArrayToStateArrays(bitsInput))));
    }

    @Test
    void iota() {
        String input = "1E 00 00 00 00 04 00 00 00 00 00 00 00 F0 01 00 " +
                "00 80 0F 00 00 04 00 00 1E 00 00 00 00 00 00 00 " +
                "00 80 0F 00 00 F0 01 00 08 00 00 00 00 00 00 00 " +
                "00 00 E0 03 00 F0 03 00 08 00 00 00 00 00 00 10 " +
                "00 00 00 00 00 F0 03 00 00 00 E0 03 00 00 00 10 " +
                "3E 00 00 00 00 00 00 00 20 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 3E 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 00 7C 00 F0 01 00 00 00 " +
                "00 00 00 00 10 00 00 00 00 7C 00 00 00 00 00 00 " +
                "00 40 00 F0 01 00 00 00 00 00 00 00 10 00 00 00 " +
                "00 00 00 00 00 1F 00 20 00 00 00 00 00 00 00 00 " +
                "7C 00 00 00 00 1F 00 00 00 00 00 00 00 02 00 20 " +
                "7C 00 00 00 00 00 00 00";
        String output = "1F 00 00 00 00 04 00 00 00 00 00 00 00 F0 01 00 " +
                "00 80 0F 00 00 04 00 00 1E 00 00 00 00 00 00 00 " +
                "00 80 0F 00 00 F0 01 00 08 00 00 00 00 00 00 00 " +
                "00 00 E0 03 00 F0 03 00 08 00 00 00 00 00 00 10 " +
                "00 00 00 00 00 F0 03 00 00 00 E0 03 00 00 00 10 " +
                "3E 00 00 00 00 00 00 00 20 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 3E 3E 00 00 00 00 00 00 " +
                "00 00 04 00 00 00 00 00 00 7C 00 F0 01 00 00 00 " +
                "00 00 00 00 10 00 00 00 00 7C 00 00 00 00 00 00 " +
                "00 40 00 F0 01 00 00 00 00 00 00 00 10 00 00 00 " +
                "00 00 00 00 00 1F 00 20 00 00 00 00 00 00 00 00 " +
                "7C 00 00 00 00 1F 00 00 00 00 00 00 00 02 00 20 " +
                "7C 00 00 00 00 00 00 00";

        byte[] bitsInput = Utils.hexStringToBitArray(input);
        byte[] bitsOutput = Utils.hexStringToBitArray(output);

        Utils.printBits(bitsOutput, "Expect Output: ");
        Utils.printBits(Keccak1600.stateArraysToBitArray(Keccak1600.iota(Keccak1600.bitArrayToStateArrays(bitsInput), 0)), "Actual Output: ");

        Assertions.assertArrayEquals(bitsOutput, Keccak1600.stateArraysToBitArray(Keccak1600.iota(Keccak1600.bitArrayToStateArrays(bitsInput), 0)));
    }

    @Test
    void bitArrayToStateArrays() {
        Random random = new Random();
        byte[] bits = new byte[5*5* Keccak1600.w];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = (byte) random.nextInt(1);
        }

        byte[][][] state = Keccak1600.bitArrayToStateArrays(bits);

        Assertions.assertArrayEquals(bits, Keccak1600.stateArraysToBitArray(state));

        state[0][0][0] = (byte) ((state[0][0][0] + 1)%2);
        bits[0] = (byte) ((bits[0] + 1)%2);
        Assertions.assertArrayEquals(bits, Keccak1600.stateArraysToBitArray(state));

        state[0][0][Keccak1600.w - 1] = (byte) ((state[0][0][Keccak1600.w - 1] + 1)%2);
        bits[Keccak1600.w - 1] = (byte) ((bits[Keccak1600.w - 1] + 1)%2);
        Assertions.assertArrayEquals(bits, Keccak1600.stateArraysToBitArray(state));

        state[1][0][0] = (byte) ((state[1][0][0] + 1)%2);
        bits[Keccak1600.w] = (byte) ((bits[Keccak1600.w] + 1)%2);
        Assertions.assertArrayEquals(bits, Keccak1600.stateArraysToBitArray(state));
    }
}
