package edu.uw.tcss487.vrdgroup.main;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Duy Vu
 * @version 0.0001
 * All utils methods
 */
public class Utils {

    /**
     * Convert a hex string to bit array
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBitArray(String hexString) {
        String[] hexStringArray = hexString.split(" ");
        byte[] byteArray = new byte[hexStringArray.length];
        for (int i = 0; i < hexStringArray.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(hexStringArray[i], 16);
        }
        return byteArrayToBitArray(byteArray);
    }

    /**
     * Create a biginteger from an array of bits
     * @param bits
     * @return
     */
    public static BigInteger bitsArrayToBigInteger(byte[] bits) {
        String bitString = "";
        for (int i = 0; i < bits.length; i++) {
            bitString += bits[i];
        }
        bitString = "0" + bitString;
        return new BigInteger(bitString, 2);
    }

    /**
     * Do a xor operation on two byte array
     * @param lane1
     * @param lane2
     * @return
     */
    public static byte[] xor2Lanes(byte[] lane1, byte[] lane2) {
        assert lane1.length == lane2.length;
        byte[] rs = new byte[lane1.length];
        for (int i = 0; i < lane1.length; i++) {
            rs[i] = (byte) (lane1[i] ^ lane2[i]);
        }
        return rs;
    }

    /**
     * Rotate a lane based on the description from https://keccak.team/files/Keccak-reference-3.0.pdf
     * @param lane
     * @param d
     * @return
     */
    public static byte[] ROT(byte[] lane, int d) {
        byte[] rs = new byte[lane.length];
        for (int i = 0; i < lane.length; i++) {
            rs[i] = lane[(i + d)%lane.length];
        }
        return rs;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static int floorMod(int x, int y) {
        int rs = x % y;
        if (rs < 0) {
            rs += y;
        }
        return rs;
    }

    /**
     * Print byte array
     * @param bits
     * @param title
     */
    public static void printBits(byte[] bits, String title) {
        System.out.println("-----------------------------");
        System.out.println(title);
        System.out.println("-----------------------------");
        String str = "";
        for (int i = 0; i < bits.length; i+=8) {
            String binaryString = "";
            for (int j = i; j < i + 8 && j < bits.length; j++) {
                binaryString += bits[j];
            }
            String hex = Integer.toHexString(Integer.parseInt("0" + binaryString, 2)).toUpperCase();
            //String hex = binaryString;
            hex = hex.length() > 1 ? hex : "0" + hex;
            if (i % (16*8) == 0) {
                str += "\n" + hex;
            } else {
                str += " " + hex;
            }
        }
        System.out.println(str);
        System.out.println("-----------------------------");
    }

    /**
     * Get hex string
     * @param bits
     */
    public static String getHexString(byte[] bits) {
        String str = "";
        for (int i = 0; i < bits.length; i+=8) {
            String binaryString = "";
            for (int j = i; j < i + 8 && j < bits.length; j++) {
                binaryString += bits[j];
            }
            String hex = Integer.toHexString(Integer.parseInt("0" + binaryString, 2)).toUpperCase();
            //String hex = binaryString;
            hex = hex.length() > 1 ? hex : "0" + hex;
            str += " " + hex;
        }
        return str;
    }

    /**
     * Convert bit array to byte array
     * @param bits
     */
    public static byte[] bitArrayToByteArray(byte[] bits) {
        assert bits.length % 8 == 0;

        byte[] rs = new byte[bits.length/8];
        for (int i = 0; i < bits.length; i+=8) {
            String binaryString = "";
            for (int j = i; j < i + 8 && j < bits.length; j++) {
                binaryString += bits[j];
            }
            rs[i/8] = (byte)Integer.parseInt("0" + binaryString, 2);
        }
        return rs;
    }

    /**
     * Convert a byte array into a bit array with using byte 0, 1 to present a bit
     * @param byteArray
     * @return
     */
    public static byte[] byteArrayToBitArray(byte[] byteArray) {
        byte[] bitArray = new byte[byteArray.length*8];
        int r = 0;
        for (int i = 0; i < byteArray.length; i++) {
            int j = 7;
            while (j >= 0) {
                byte mask = (byte) 0x01;
                mask = (byte) (mask << j);
                if ((mask & byteArray[i]) != 0) {
                    bitArray[r] = 1;
                } else {
                    bitArray[r] = 0;
                }
                r++;
                j--;
            }
        }
        return bitArray;
    }

    /**
     * Print state
     * @param A
     * @param title
     */
    public static void printState(byte[][][] A, String title) {
        System.out.println("-----------------------------");
        System.out.println(title);
        System.out.println("-----------------------------");
        String[][] lane = new String[5][5];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (lane[x][y] == null) {
                    lane[x][y] = "";
                }
                lane[x][y] += " " + getHexString(A[x][y]);
            }
        }
        String[] plane = new String[] {"", "", "", "", ""};
        for (int j = 0; j < 5; j++) {
            plane[j] += " " + lane[0][j] + " " + lane[1][j] + " " + lane[2][j] + " " + lane[3][j] + " " + lane[4][j] + "\n";
        }
        String str = plane[0] + " " + plane[1] + " " + plane[2] + " " + plane[3] + " " + plane[4];
        System.out.println(str);
        System.out.println("-----------------------------");
    }

    /**
     * Concat two byte arrays
     * @param a1
     * @param a2
     * @return
     */
    public static byte[] concat(byte[] a1, byte[] a2) {
        byte[] rs = new byte[a1.length + a2.length];
        System.arraycopy(a1, 0, rs, 0, a1.length);
        System.arraycopy(a2, 0, rs, a1.length, a2.length);
        return rs;
    }

    /**
     * Check if two byte arrays equal
     * @param a1
     * @param a2
     * @return
     */
    public static boolean equals(byte[] a1, byte[] a2) {
        if (a1.length != a2.length) return false;
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get text input from console
     * @return
     */
    public static String getTextInput() {
        Scanner input = new Scanner(System.in);
        List<String> lines = new ArrayList<String>();
        String lineNew;

        while (input.hasNextLine()) {
            lineNew = input.nextLine();
            if (lineNew.isEmpty()) {
                break;
            }
            lines.add(lineNew);
        }
        String text = "";
        for (String string : lines) {
            text += string + "\n";
        }
        return text;
    }

    /**
     * Read file content into a byte array
     * @param filePath
     * @return
     */
    public static byte[] readFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) return null;

        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];

        try
        {
            //Read bytes with InputStream
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();

            return bFile;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Write byte array into a file
     * @param filePath
     * @return
     */
    public static boolean writeFile(String filePath, byte[] data) {
        File file = new File(filePath);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Read object from file
     * @param filePath
     * @return
     */
    public static Object readObject(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) return null;

        FileInputStream in = null;
        ObjectInputStream inObj = null;
        try {
            in = new FileInputStream(file);
            inObj = new ObjectInputStream(in);
            Object object = inObj.readObject();
            inObj.close();
            in.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Save object to file
     * @param filePath
     * @param object
     * @return
     */
    public static boolean writeObject(String filePath, Serializable object) {
        final File file = new File(filePath);
        FileOutputStream out = null;
        ObjectOutputStream outObj = null;
        try {
            out = new FileOutputStream(file);
            outObj = new ObjectOutputStream(out);
            outObj.writeObject(object);
            outObj.close();
            out.close();
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Symmetric cryptogram data structure
     */
    public static class SymmetricCryptogram implements Serializable {

        public byte[] z;
        public byte[] c;
        public byte[] t;

        public SymmetricCryptogram(byte[] z, byte[] c, byte[] t) {
            this.z = z;
            this.c = c;
            this.t = t;
        }
    }

    /**
     * Elliptic cryptogram data structure
     */
    public static class EllipticCryptogram implements Serializable {

        public EdwardsPoint Z;
        public byte[] c;
        public byte[] t;

        public EllipticCryptogram(EdwardsPoint Z, byte[] c, byte[] t) {
            this.Z = Z;
            this.c = c;
            this.t = t;
        }
    }

    /**
     * Elliptic private/public key pair
     */
    public static class EllipticKeyPair implements Serializable {

        public BigInteger s;
        public EdwardsPoint V;

        public EllipticKeyPair(BigInteger s, EdwardsPoint V) {
            this.s = s;
            this.V = V;
        }
    }

    /**
     * Signature
     */
    public static class Signature implements Serializable {

        public BigInteger h;
        public BigInteger z;

        public Signature(BigInteger h, BigInteger z) {
            this.h = h;
            this.z = z;
        }
    }
}
