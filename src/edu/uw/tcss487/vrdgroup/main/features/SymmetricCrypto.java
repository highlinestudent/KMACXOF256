package edu.uw.tcss487.vrdgroup.main.features;

import edu.uw.tcss487.vrdgroup.main.Main;
import edu.uw.tcss487.vrdgroup.main.SHA3;
import edu.uw.tcss487.vrdgroup.main.Utils;

import java.security.SecureRandom;

/**
 * @author Vu Nguyen
 * @version 0.0001
 * All symmetric crypto features
 */
public class SymmetricCrypto {

    /**
     * Encrypt symmetrically a message with a passphrase
     * @param m
     * @param pw
     */
    public static Utils.SymmetricCryptogram encryptSymmetrically(byte[] m, byte[] pw) {
        SecureRandom rand = new SecureRandom();
        byte[] z = new byte[512/4];
        rand.nextBytes(z);
        byte[] ke_ka = SHA3.KMACXOF256(Utils.concat(z, pw), "".getBytes(), 1024, "S".getBytes());
        byte[] ke = new byte[512/8];
        System.arraycopy(ke_ka, 0, ke, 0, 512/8);
        byte[] ka = new byte[512/8];
        System.arraycopy(ke_ka, 512/8, ka, 0, 512/8);
        byte[] c = Utils.xor2Lanes(SHA3.KMACXOF256(ke, "".getBytes(), m.length*8, "SKE".getBytes()), m);
        byte[] t = SHA3.KMACXOF256(ka, m, 512, "SKA".getBytes());

        return new Utils.SymmetricCryptogram(z, c, t);
    }

    /**
     * Decrypt symmetrically with a given passphrase
     * @param pw
     */
    public static byte[] decryptSymmetrically(Utils.SymmetricCryptogram cryptogram, byte[] pw) {
        byte[] ke_ka = SHA3.KMACXOF256(Utils.concat(cryptogram.z, pw), "".getBytes(), 1024, "S".getBytes());
        byte[] ke = new byte[512/8];
        System.arraycopy(ke_ka, 0, ke, 0, 512/8);
        byte[] ka = new byte[512/8];
        System.arraycopy(ke_ka, 512/8, ka, 0, 512/8);
        byte[] d_m = Utils.xor2Lanes(SHA3.KMACXOF256(ke, "".getBytes(), cryptogram.c.length*8, "SKE".getBytes()), cryptogram.c);
        byte[] t_prime = SHA3.KMACXOF256(ka, d_m, 512, "SKA".getBytes());
        if (Utils.equals(cryptogram.t, t_prime)) return d_m;
        return null;
    }
}
