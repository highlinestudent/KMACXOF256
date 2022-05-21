package edu.uw.tcss487.vrdgroup.main;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        byte[] ans = left_encode(BigInteger.valueOf(1234567));
        for (int i = 0; i < ans.length; i++) {
            System.out.println(255 & ans[i]);
        }
    }

    /**
     * Encodes the integer x as a byte string in a way that can be unambiguously parsed
     * from the end of the string by inserting the length of the byte string after the byte string
     * representation of x.
     * @param x int
     * @return byte[]
     */
    public static byte[] right_encode(BigInteger x) {
//        Validity Conditions: 0 ≤ x < 2^2040
        assert x.compareTo(new BigInteger("0")) >= 0 && x.compareTo(new BigInteger("2").pow(2040)) < 0;

//        1. Let n be the smallest positive integer for which 2^(8n) > x.
        int n = findNForLeftRightEncode(x);
//        2. Let x1, x2,…, xn be the base-256 encoding of x satisfying:
//        x = ∑ 2^(8(n-i))*x_i, for i = 1 to n.
        byte[] x_n = base256(x, n);
//        3. Let Oi = enc8(x_i), for i = 1 to n.
        byte[] rs = new byte[n + 1];
        for (int i = 0; i < x_n.length; i++) {
            rs[i] = enc8(x_n[i]);
        }
//        4. Let On+1 = enc8(n).
        rs[n] = enc8(n);
//        5. Return O = O1 || O2 || … || On || On+1.
        return rs;
    }

    /**
     * Encodes the integer x as a byte string in a way that can be unambiguously parsed
     * from the beginning of the string by inserting the length of the byte string before the byte string
     * representation of x.
     * @param x int
     * @return byte[]
     */
    public static byte[] left_encode(BigInteger x) {
//        Validity Conditions: 0 ≤ x < 2^2040
        assert x.compareTo(new BigInteger("0")) >= 0 && x.compareTo(new BigInteger("2").pow(2040)) < 0;

//        1. Let n be the smallest positive integer for which 2^(8n) > x.
        int n = findNForLeftRightEncode(x);
//        2. Let x1, x2, …, xn be the base-256 encoding of x satisfying:
//        x = ∑ 2^(8(n-i))*x_i, for i = 1 to n.
        byte[] x_n = base256(x, n);
//        3. Let Oi = enc8(x_i), for i = 1 to n.
        byte[] rs = new byte[n + 1];
        for (int i = 0; i < x_n.length; i++) {
            rs[i + 1] = enc8(x_n[i]);
        }
//        4. Let O0 = enc8(n).
        rs[0] = enc8(n);
//        5. Return O = O0 || O1 || … || On−1 || On.

        return rs;
    }

    /**
     * The encode_string function is used to encode bit strings in a way that may be parsed
     * unambiguously from the beginning of the string, S.
     * @param S byte[]
     * @return byte[]
     */
    public static byte[] encode_string(byte[] S) {
//        Validity Conditions: 0 ≤ len(S) < 2^2040
        assert BigInteger.valueOf(S.length).compareTo(new BigInteger("0")) >= 0
                && BigInteger.valueOf(S.length).compareTo(new BigInteger("2").pow(2040)) < 0;

//        1. Return left_encode(len(S)) || S.
        byte[] leftEncode = left_encode(BigInteger.valueOf(S.length));
        byte[] rs = new byte[leftEncode.length + S.length];
        System.arraycopy(leftEncode, 0, rs, 0, leftEncode.length);
        System.arraycopy(S, 0, rs, leftEncode.length, S.length);

        return rs;
    }

    /**
     * The bytepad(X, w) function prepends an encoding of the integer w to an input string X, then pads
     * the result with zeros until it is a byte string whose length in bytes is a multiple of w. In general,
     * bytepad is intended to be used on encoded strings—the byte string bytepad(encode_string(S), w)
     * can be parsed unambiguously from its beginning, whereas bytepad does not provide
     * unambiguous padding for all input strings.
     * @param X byte[]
     * @param w int
     * @return byte[]
     */
    public static byte[] bytepad(byte[] X, int w) {
//        Validity Conditions: w > 0
        assert w > 0;
//
//        1. z = left_encode(w) || X.
        byte[] wenc = left_encode(BigInteger.valueOf(w));
        //Calculate total bytes needed, include for step 2 and 3
        byte[] z = new byte[w*((wenc.length + X.length + w - 1)/w)];
        //Append X to z
        System.arraycopy(wenc, 0, z, 0, wenc.length);
        System.arraycopy(X, 0, z, wenc.length, X.length);
//        2. while len(z) mod 8 ≠ 0:
//        z = z || 0
//        3. while (len(z)/8) mod w ≠ 0:
//        z = z || 00000000
        //Set the rest to 0, this do step 2 and 3 at the same time
        for (int i = wenc.length + X.length; i < z.length; i++) {
            z[i] = (byte)0;
        }
//        4. return z.
        return z;
    }

    /**
     * Convert an integer to base 64 byte array
     * @param x
     * @param length
     * @return
     */
    public static byte[] base256(BigInteger x, int length) {
        BigInteger xx = x;
        int i = length - 1;
        byte[] rs = new byte[length];

        while (xx.compareTo(new BigInteger("0")) > 0) {
            rs[i--] = xx.mod(new BigInteger("256")).byteValue();
            xx = xx.divide(new BigInteger("256"));
        }
        return rs;
    }

    /**
     * Encode 8 an integer and return a byte represent the bit sequence
     * @param x
     * @return
     */
    public static byte enc8(int x) {
        assert (255 & x) >= 0 && (255 & x) <= 255;

        x = 255 & x;

        byte b = 0;
        //be careful, have to use an integer for mask
        int mask = 128;

        while (x > 0) {
            if (x % 2 == 1) {
                b = (byte) (b | mask);
            }
            x = x / 2;
            mask = (byte) (mask >> 1);
        }

        return b;
    }

    /**
     * Find n be the smallest positive integer for which 2^(8n) > x
     * @param x
     * @return
     */
    public static int findNForLeftRightEncode(BigInteger x) {
        if (x.compareTo(BigInteger.valueOf(0)) == 0) return 1;
        BigInteger xx = x;
        int n = 0;
        while(xx.compareTo(BigInteger.valueOf(0)) != 0){
            n ++;
            xx = xx.divide(BigInteger.valueOf(256));
        }
        return n;
    }

    /**
     * To implement SHA-3 we only care below values from KECCAK permutation
     */
    private static int b = 1600;
    private static int w = 64;
    private static int l = 6;

    /**
     * Instead work with string bits, we will work with byte array
     * To present 1600 bits, we need a byte array with length 200
     * We also don't use state array A, we will modify directly byte array S
     * Using the formula: A[x, y, x] = S[w(5y+x)+z]
     */

    /**
     * Get bit value at index of bit string, need to convert this index to byte array index and offset of a byte
     * @param index
     * @param S
     * @return byte number 0x01, 0x00 (be careful)
     */
    public static byte getBitAt(int index, byte[] S) {
        //we only work with array has length 200
        assert S.length == 200;
        //index must be smaller then 1600
        assert index < 1600;

        int i = index / 8;
        int offset = index % 8;

        int mask = 0x01 << (8 - offset);

        return (mask & S[index]) != 0x00 ? (byte)0x01 : (byte)0x00;
    }

    /**
     * Get bit from matrix coordinate
     * @param x
     * @param y
     * @param z
     * @param S
     * @return
     */
    public static byte getBitAt(int x, int y, int z, byte[] S) {
        //Formula: A[x, y, z] = S[w(5y+x)+z]
        return getBitAt(w*(5*y+x)+z, S);
    }

    /**
     * Set bit value at index of bit string, need to convert this index to byte array index and offset of a byte
     * @param index
     * @param S
     * @param value byte number 0x01, 0x00 (be careful)
     */
    public static void setBitAt(int index, byte[] S, byte value) {
        //we only work with array has length 200
        assert S.length == 200;
        //index must be smaller then 1600
        assert index < 1600;

        int i = index / 8;
        int offset = index % 8;

        //Turn on bit at index
        if (value == 0x01) {
            S[i] = (byte)((0x01 << (8 - offset)) | S[i]);
        }
        //Turn off bit at index
        else {
            S[i] = (byte)((~(0x01 << (8 - offset))) & S[i]);
        }
    }

    /**
     * Set bit from matrix coordinate
     * @param x
     * @param y
     * @param z
     * @param S
     * @param value
     */
    public static void setBitAt(int x, int y, int z, byte[] S, byte value) {
        //Formula: A[x, y, z] = S[w(5y+x)+z]
        setBitAt(w*(5*y+x)+z, S, value);
    }

    /**
     * Implement theta step, we do exactly the description in NIST.FIPS.202
     * @param S
     * @return
     */
    public static byte[] theta(byte[] S) {
        byte[][] C = new byte[5][w];
        byte[][] D = new byte[5][w];
        byte[] SS = new byte[S.length];

        //Formula: A[x, y, z] = S[w(5y+x)+z]

        //1. For all pairs (x,z) such that 0≤x<5 and 0≤z<w, let
        //C[x,z]=A[x, 0,z] ⊕ A[x, 1,z] ⊕ A[x, 2,z] ⊕ A[x, 3,z] ⊕ A[x, 4,z].
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < w; j++) {
                C[i][j] = (byte) (getBitAt(i, 0, j, S)
                                            ^ getBitAt(i, 1, j, S)
                                            ^ getBitAt(i, 2, j, S)
                                            ^ getBitAt(i, 3, j, S)
                                            ^ getBitAt(i, 4, j, S));
            }
        }

        //2. For all pairs (x, z) such that 0≤x<5 and 0≤z<w let
        //D[x,z]=C[(x1) mod 5, z] ⊕ C[(x+1) mod 5, (z –1) mod w].
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < w; j++) {
                D[i][j] = (byte) (C[(i-1)%5][j] ^ C[(i+1)%5][(j-1)%w]);
            }
        }

        //3. For all triples (x, y, z) such that 0≤x<5, 0≤y<5, and 0≤z<w, let
        //A′[x, y,z] = A[x, y,z] ⊕ D[x,z].
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < w; k++) {
                    //SS[w*(5*j+i)+k] = (byte) (S[w*(5*j+i)+k] ^ D[i][k]);
                    setBitAt(i, j, k, SS, (byte) (getBitAt(i, j, k, S) ^ D[i][k]));
                }
            }
        }
        return SS;
    }

    /**
     * Implement the roh step, we do exactly the description in NIST.FIPS.202
     * @param S
     * @return
     */
    public static byte[] roh(byte[] S) {
        byte[] SS = new byte[S.length];

        //Formula: A[x, y, z] = S[w(5y+x)+z]

        //1. For all z such that 0≤z<w, let A′ [0, 0,z] = A[0, 0,z]
        for (int i = 0; i < w; i++) {
            //SS[w*(5*0+0)+i] = S[w*(5*0+0)+i];
            setBitAt(0, 0, i, SS, getBitAt(0, 0, i, S));
        }

        //2. Let (x, y) = (1, 0).
        int x = 1, y = 0;

        //3. For t from 0 to 23:
        //a. for all z such that 0≤z<w, let A′[x, y,z] = A[x, y, (z–(t+1)(t+2)/2) mod w];
        //b. let (x, y) = (y, (2x+3y) mod 5).
        for (int t = 0; t <= 23; t++) {
            for (int i = 0; i < w; i++) {
                //SS[w * (5 * y + x) + i] = S[w*(5*y+x)+((i-(t+1)*(t+2)/2)%w)];
                setBitAt(x, y, i, SS, getBitAt(x, y, (i-(t+1)*(t+2)/2)%w, S));
                x = y;
                y = (2*x+3*y)%5;
            }
        }
        return SS;
    }

    /**
     * Implement the pi step, we do exactly the description in NIST.FIPS.202
     * @param S
     * @return
     */
    public static byte[] pi(byte[] S) {
        byte[] SS = new byte[S.length];

        //Formula: A[x, y, z] = S[w(5y+x)+z]

        //1. For all triples (x, y, z) such that 0≤x<5, 0≤y<5, and 0≤z<w, let
        //A′[x, y, z]=A[(x + 3y) mod 5, x, z].
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < w; k++) {
                    setBitAt(i, j, k, SS, getBitAt((i+3*j)%5, i, k, S));
                }
            }
        }
        return SS;
    }

    /**
     * Implement the chi step, we do exactly the description in NIST.FIPS.202
     * @param S
     * @return
     */
    public static byte[] chi(byte[] S) {
        byte[] SS = new byte[S.length];

        //Formula: A[x, y, z] = S[w(5y+x)+z]

        //For all triples (x, y, z) such that 0≤x<5, 0≤y<5, and 0≤z<w, let
        //A′[x, y,z] = A[x, y,z] ⊕ ((A[(x+1) mod 5, y, z] ⊕ 1) ⋅ A[(x+2) mod 5, y, z]).
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < w; k++) {
                    setBitAt(i, j, k, SS, (byte) (getBitAt(i, j, k, S) ^ ((getBitAt((i+1)%5, j, k, S) ^ 0x01) & getBitAt((i+2)%5, j, k, S))));
                }
            }
        }
        return SS;
    }
}
