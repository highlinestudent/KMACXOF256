package edu.uw.tcss487.vrdgroup.main.features;

import edu.uw.tcss487.vrdgroup.main.EdwardsPoint;
import edu.uw.tcss487.vrdgroup.main.SHA3;
import edu.uw.tcss487.vrdgroup.main.Utils;
import org.junit.jupiter.api.Assertions;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Duy Vu
 * @version 0.0001
 * Signature feature functions
 */
public class Signature {

    /**
     * Generate signature of a message under password phrase
     * @param m
     * @param pw
     */
    public static Utils.Signature sign(byte[] m, byte[] pw) {
        BigInteger s = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(pw, "".getBytes(), 512, "K".getBytes()))).multiply(BigInteger.valueOf(4));
        BigInteger k = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(s.toByteArray(), m, 512, "N".getBytes()))).multiply(BigInteger.valueOf(4));
        EdwardsPoint U = EllipticCrypto.G.multiply(k);
        BigInteger h = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(U.x.toByteArray(), m, 512, "T".getBytes())));
        BigInteger z = k.subtract(h.multiply(s)).mod(EdwardsPoint.R);

        return new Utils.Signature(h, z);
    }

    /**
     * Sign a message m using private key s
     * @param m
     * @param s
     * @return
     */
    public static Utils.Signature sign(byte[] m, BigInteger s) {
        SecureRandom rand = new SecureRandom();
        byte[] kk = new byte[512/4];
        rand.nextBytes(kk);

        BigInteger k = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(kk)).multiply(BigInteger.valueOf(4));
        EdwardsPoint U = EllipticCrypto.G.multiply(k);
        BigInteger h = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(U.x.toByteArray(), m, 512, "T".getBytes())));
        BigInteger z = k.subtract(h.multiply(s)).mod(EdwardsPoint.R);

        return new Utils.Signature(h, z);
    }

    /**
     * Verify signature for a byte array m under the public key V
     * @param m
     * @param V
     * @return
     */
    public static boolean verifySignature(Utils.Signature sig, byte[] m, EdwardsPoint V) {
        EdwardsPoint U = EllipticCrypto.G.multiply(sig.z).add(V.multiply(sig.h));
        return Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(U.x.toByteArray(), m, 512, "T".getBytes()))).equals(sig.h);
    }
}
