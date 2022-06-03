package edu.uw.tcss487.vrdgroup.main.features;

import edu.uw.tcss487.vrdgroup.main.SHA3;
import edu.uw.tcss487.vrdgroup.main.Utils;

/**
 * @author Duy Vu
 * @version 0.0001
 * All hash features
 */
public class Hash {
    /**
     * Compute hash for a byte array
     * @param m
     * @return
     */
    public static byte[] computeHash(byte[] m) {
        if (m == null) return null;

        byte[] h = SHA3.KMACXOF256("".getBytes(), m, 512, "D".getBytes());
        return h;
    }

    /**
     * Compute hash for a file
     * @param filePath
     * @return
     */
    public static byte[] computeHash(String filePath) {
        return computeHash(Utils.readFile(filePath));
    }

    /**
     * Compute authenticate tag of a byte array under passphrase pw
     * @param m
     * @param pw
     * @return
     */
    public static byte[] computeAuthenticateTag(byte[] m, byte[] pw) {
        byte[] t = SHA3.KMACXOF256(pw, m, 512, "T".getBytes());
        return t;
    }
}
