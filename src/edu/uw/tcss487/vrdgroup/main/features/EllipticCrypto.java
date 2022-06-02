package edu.uw.tcss487.vrdgroup.main.features;

import edu.uw.tcss487.vrdgroup.main.EdwardsPoint;
import edu.uw.tcss487.vrdgroup.main.SHA3;
import edu.uw.tcss487.vrdgroup.main.Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Vu Nguyen
 * @version 0.0001
 * All elliptic features
 */
public class EllipticCrypto {

    public static EdwardsPoint G = new EdwardsPoint(BigInteger.valueOf(4), false);

    /**
     * Encrypt a message with a elliptic public key
     * @param m
     * @param V
     */
    public static Utils.EllipticCryptogram ellipticEncrypt(byte[] m, EdwardsPoint V) {
        SecureRandom rand = new SecureRandom();

        byte[] kk = new byte[512/4];
        rand.nextBytes(kk);
        BigInteger k = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(kk)).multiply(BigInteger.valueOf(4));
        EdwardsPoint W = V.multiply(k);
        EdwardsPoint Z = G.multiply(k);
        byte[] ke_ka = SHA3.KMACXOF256(W.x.toByteArray(), "".getBytes(), 1024, "P".getBytes());
        byte[] ke = new byte[512/8];
        System.arraycopy(ke_ka, 0, ke, 0, 512/8);
        byte[] ka = new byte[512/8];
        System.arraycopy(ke_ka, 512/8, ka, 0, 512/8);
        byte[] c = Utils.xor2Lanes(SHA3.KMACXOF256(ke, "".getBytes(), m.length*8, "PKE".getBytes()), m);
        byte[] t = SHA3.KMACXOF256(ka, m, 512, "PKA".getBytes());

        return new Utils.EllipticCryptogram(Z, c, t);
    }

    /**
     * Decrypt elliptic public key encrypted message with a given passphrase
     * @param pw
     */
    public static byte[] ellipticDecrypt(Utils.EllipticCryptogram cryptogram, byte[] pw) {
        BigInteger s = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(pw, "".getBytes(), 512, "K".getBytes()))).multiply(BigInteger.valueOf(4));
        EdwardsPoint W = cryptogram.Z.multiply(s);
        byte[] ke_ka = SHA3.KMACXOF256(W.x.toByteArray(), "".getBytes(), 1024, "P".getBytes());
        byte[] ke = new byte[512/8];
        System.arraycopy(ke_ka, 0, ke, 0, 512/8);
        byte[] ka = new byte[512/8];
        System.arraycopy(ke_ka, 512/8, ka, 0, 512/8);
        byte[] d_m = Utils.xor2Lanes(SHA3.KMACXOF256(ke, "".getBytes(), cryptogram.c.length*8, "PKE".getBytes()), cryptogram.c);
        byte[] t_prime = SHA3.KMACXOF256(ka, d_m, 512, "PKA".getBytes());
        Utils.printBits(Utils.byteArrayToBitArray(cryptogram.t), "t");
        Utils.printBits(Utils.byteArrayToBitArray(t_prime), "t'");
        if (Utils.equals(cryptogram.t, t_prime)) return d_m;
        return null;
    }

    /**
     * Generate Elliptic key pair from a passphrase
     * @param pw
     * @return
     */
    public static Utils.EllipticKeyPair generateEllipticKeys(byte[] pw) {
        BigInteger s = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(pw, "".getBytes(), 512, "K".getBytes()))).multiply(BigInteger.valueOf(4));
        EdwardsPoint V = G.multiply(s);

        return new Utils.EllipticKeyPair(s, V);
    }
}
