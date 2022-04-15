package edu.uw.tcss487.vrdgroup.main;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }

    /**
     * Encodes the integer x as a byte string in a way that can be unambiguously parsed
     * from the end of the string by inserting the length of the byte string after the byte string
     * representation of x.
     * @param x int
     * @return byte[]
     */
    public static byte[] right_encode(int x) {
//        Validity Conditions: 0 ≤ x < 2^2040

//        1. Let n be the smallest positive integer for which 2^(8n) > x.
//        2. Let x1, x2,…, xn be the base-256 encoding of x satisfying:
//        x = ∑ 2^(8(n-i))*x_i, for i = 1 to n.
//        3. Let Oi = enc8(x_i), for i = 1 to n.
//        4. Let On+1 = enc8(n).
//        5. Return O = O1 || O2 || … || On || On+1.
        return null;
    }

    /**
     * Encodes the integer x as a byte string in a way that can be unambiguously parsed
     * from the beginning of the string by inserting the length of the byte string before the byte string
     * representation of x.
     * @param x int
     * @return byte[]
     */
    public static byte[] left_encode(int x) {
//        Validity Conditions: 0 ≤ x < 2^2040

//        1. Let n be the smallest positive integer for which 2^(8n) > x.
//        2. Let x1, x2, …, xn be the base-256 encoding of x satisfying:
//        x = ∑ 2^(8(n-i))*x_i, for i = 1 to n.
//        3. Let Oi = enc8(x_i), for i = 1 to n.
//        4. Let O0 = enc8(n).
//        5. Return O = O0 || O1 || … || On−1 || On.

        return null;
    }

    /**
     * The encode_string function is used to encode bit strings in a way that may be parsed
     * unambiguously from the beginning of the string, S.
     * @param S byte[]
     * @return byte[]
     */
    public static byte[] encode_string(byte[] S) {
//        Validity Conditions: 0 ≤ len(S) < 2^2040

//        1. Return left_encode(len(S)) || S.

        return null;
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
//
//        1. z = left_encode(w) || X.
//        2. while len(z) mod 8 ≠ 0:
//        z = z || 0
//        3. while (len(z)/8) mod w ≠ 0:
//        z = z || 00000000
//        4. return z.

        return null;
    }
}
