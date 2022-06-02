package edu.uw.tcss487.vrdgroup.main;

import java.math.BigInteger;

/**
 * @author Vu Nguyen
 * This class contains all method required for KMACXOF256
 */
public class SHA3 {
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
     * This method from the lecture
     *
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
     * SHAKE256(M, d) = KECCAK[512] (M || 1111, d).
     * @param N bit array, not byte
     * @param d
     * @return
     */
    public static byte[] SHAKE256(byte[] N, int d) {
        byte[] tmp = new byte[N.length + 4];
        System.arraycopy(N, 0, tmp, 0, N.length);
        tmp[tmp.length - 1] = 1;
        tmp[tmp.length - 2] = 1;
        tmp[tmp.length - 3] = 1;
        tmp[tmp.length - 4] = 1;

        byte[] rs = Keccak1600.KECCAK(512, tmp, d);

        //printBits(rs, "KECCAK(512, tmp, " + d + ")");

        return rs;
    }

    /**
     *
     * @param X is the main input bit string. It may be of any length
     * , including zero
     * @param L is an integer representing the requested output length in bits
     * @param N is a function-name bit string, used by NIST to define functions based on cSHAKE.
     * When no function other than cSHAKE is desired, N is set to the empty string.
     * @param S is a customization bit string. The user selects this string to define a variant of the
     * function. When no customization is desired, S is set to the empty string
     * @return byte[]
     */
    public static byte[] cSHAKE256(byte[] X, int L, byte[] N, byte[] S) {
        assert N.length < Math.pow(2, 2040) && S.length < Math.pow(2, 2040);

        //1. If N = "" and S = "":
        //  return SHAKE256(X, L);
        if (N.length == 0 && S.length == 0) {
            return SHAKE256(X, L);
        }
        //2. Else:
        //  return KECCAK[512](bytepad(encode_string(N) || encode_string(S), 136) || X || 00, L).
        else {
            byte[] encodeN = encode_string(N);
            byte[] encodeS = encode_string(S);
            byte[] encodeNS = new byte[encodeN.length + encodeS.length];
            System.arraycopy(encodeN, 0, encodeNS, 0, encodeN.length);
            System.arraycopy(encodeS, 0, encodeNS, encodeN.length, encodeS.length);
            byte[] padding = bytepad(encodeNS, 136);
            byte[] M = new byte[padding.length + X.length + 2];
            System.arraycopy(padding, 0, M, 0, padding.length);
            System.arraycopy(X, 0, M, padding.length, X.length);
            M[M.length - 1] = 0;
            M[M.length - 1] = 0;

            return Keccak1600.KECCAK(512, Utils.byteArrayToBitArray(M), L);
        }
    }

    /**
     *
     * @param K is a key bit string of any length
     * , including zero
     * @param X is the main input bit string. It may be of any length, including zero.
     * @param L is an integer representing the requested output length in bits
     * @param S is an optional customization bit string of any length, including zero. If no customization
     * is desired, S is set to the empty string.
     * @return byte[]
     */
    public static byte[] KMACXOF256(byte[] K, byte[] X, int L, byte[] S) {
        assert K.length < Math.pow(2, 2040) && L >= 0 && S.length < Math.pow(2, 2040);

        byte[] padding = bytepad(encode_string(K), 136);
        byte[] r_encode = right_encode(BigInteger.valueOf(0));
        byte[] newX = new byte[padding.length + X.length + r_encode.length];
        System.arraycopy(padding, 0, newX, 0, padding.length);
        System.arraycopy(X, 0, newX, padding.length, X.length);
        System.arraycopy(r_encode, 0, newX, padding.length + X.length, r_encode.length);

        return Utils.bitArrayToByteArray(cSHAKE256(newX, L, "KMAC".getBytes(), S));
    }


}
