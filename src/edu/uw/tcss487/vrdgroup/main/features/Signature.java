package edu.uw.tcss487.vrdgroup.main.features;

import edu.uw.tcss487.vrdgroup.main.EdwardsPoint;
import edu.uw.tcss487.vrdgroup.main.SHA3;
import edu.uw.tcss487.vrdgroup.main.Utils;
import org.junit.jupiter.api.Assertions;

import java.math.BigInteger;
import java.security.SecureRandom;

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
     * Verify signature for a byte array m under the public key V
     * @param m
     * @param V
     * @return
     */
    public static boolean verifySignature(Utils.Signature sig, byte[] m, EdwardsPoint V) {
        EdwardsPoint U = EllipticCrypto.G.multiply(sig.z).add(V.multiply(sig.h));
        return Utils.byteArrayToBitArray(SHA3.KMACXOF256(U.x.toByteArray(), m, 512, "T".getBytes())).equals(sig.h);
    }
}
