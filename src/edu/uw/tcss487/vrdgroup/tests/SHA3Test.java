package edu.uw.tcss487.vrdgroup.tests;

import edu.uw.tcss487.vrdgroup.main.EdwardsPoint;
import edu.uw.tcss487.vrdgroup.main.SHA3;
import edu.uw.tcss487.vrdgroup.main.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Vu Nguyen
 * Unit tests for SHA3 derived function KMACXOF256
 */
class SHA3Test {

    @Test
    void right_encode() {
        Assertions.assertArrayEquals(
            //binary: 00000000 1000000
            new byte[]{
                    0,
                    (byte)0x80
            },
            SHA3.right_encode(new BigInteger("0"))
        );
    }

    @Test
    void left_encode() {
        Assertions.assertArrayEquals(
            //binary: 10000000 0000000
            new byte[]{
                    (byte)0x80,
                    0
            },
            SHA3.left_encode(new BigInteger("0"))
        );

        Assertions.assertArrayEquals(
            //binary: 10000000 0000000
            new byte[]{
                    (byte)-64,
                    (byte)72,
                    (byte)107,
                    (byte)-31
            },
            SHA3.left_encode(new BigInteger("1234567"))
        );
    }

    @Test
    void encode_string() {
        Assertions.assertArrayEquals(SHA3.encode_string("".getBytes()),
            //binary: 10000000 0000000
            new byte[]{
                (byte)0x80,
                0
            }
        );
    }

    @Test
    void bytepad() {
        //Assume it is correct because professor provides it to us
    }

    @Test
    void base256() {
        Assertions.assertArrayEquals(SHA3.base256(new BigInteger("0"), 1),
            new byte[]{
               0
            }
        );
        Assertions.assertArrayEquals(SHA3.base256(new BigInteger("1"), 1),
            new byte[]{
                1
            }
        );
        Assertions.assertArrayEquals(SHA3.base256(new BigInteger("255"), 1),
            new byte[]{
                (byte)255
            }
        );
        Assertions.assertArrayEquals(SHA3.base256(new BigInteger("256"), 2),
            new byte[]{
                (byte)1,
                    (byte)0
            }
        );
        Assertions.assertArrayEquals(SHA3.base256(new BigInteger("1234567"), 3),
                new byte[]{
                        (byte)18,
                        (byte)214,
                        (byte)135
                }
        );
    }
    @Test
    void enc8() {
        Assertions.assertEquals((byte)0, SHA3.enc8(0));
        Assertions.assertEquals((byte)-128, SHA3.enc8(1));
        Assertions.assertEquals((byte)64, SHA3.enc8(2));
        Assertions.assertEquals((byte)72, SHA3.enc8(18));
        Assertions.assertEquals((byte)107, SHA3.enc8(214));
        Assertions.assertEquals((byte)225, SHA3.enc8(135));
    }

    @Test
    void findNForLeftRightEncode() {
        Assertions.assertEquals(1, SHA3.findNForLeftRightEncode(BigInteger.valueOf(0)));
        Assertions.assertEquals(1, SHA3.findNForLeftRightEncode(BigInteger.valueOf(1)));
        Assertions.assertEquals(2, SHA3.findNForLeftRightEncode(BigInteger.valueOf(256)));
        Assertions.assertEquals(3, SHA3.findNForLeftRightEncode(BigInteger.valueOf(202320)));
    }

    @Test
    void KMACXOF256() {
        SecureRandom rand = new SecureRandom();
        EdwardsPoint G = new EdwardsPoint(BigInteger.valueOf(4), false);
        byte[] pw = "My Password".getBytes();
        byte[] m = "My Message".getBytes();
        //Computing a cryptographic hash h of a byte array m:
        byte[] h = SHA3.KMACXOF256("".getBytes(), m, 512, "D".getBytes());
        //Compute an authentication tag t of a byte array m under passphrase pw:
        byte[] t = SHA3.KMACXOF256(pw, m, 512, "T".getBytes());
        //Encrypting a byte array m symmetrically under passphrase pw:
        byte[] z = new byte[512/4];
        rand.nextBytes(z);
        byte[] ke_ka = SHA3.KMACXOF256(Utils.concat(z, pw), "".getBytes(), 1024, "S".getBytes());
        byte[] ke = new byte[512/8];
        System.arraycopy(ke_ka, 0, ke, 0, 512/8);
        byte[] ka = new byte[512/8];
        System.arraycopy(ke_ka, 512/8, ka, 0, 512/8);
        byte[] c = Utils.xor2Lanes(SHA3.KMACXOF256(ke, "".getBytes(), m.length*8, "SKE".getBytes()), m);
        t = SHA3.KMACXOF256(ka, m, 512, "SKA".getBytes());
        //Decrypting a symmetric cryptogram (z, c, t) under passphrase pw:
        ke_ka = SHA3.KMACXOF256(Utils.concat(z, pw), "".getBytes(), 1024, "S".getBytes());
        System.arraycopy(ke_ka, 0, ke, 0, 512/8);
        System.arraycopy(ke_ka, 512/8, ka, 0, 512/8);
        byte[] d_m = Utils.xor2Lanes(SHA3.KMACXOF256(ke, "".getBytes(), c.length*8, "SKE".getBytes()), c);
        byte[] t_prime = SHA3.KMACXOF256(ka, d_m, 512, "SKA".getBytes());
        Utils.printBits(Utils.byteArrayToBitArray(t), "t");
        Utils.printBits(Utils.byteArrayToBitArray(t_prime), "t'");
        Assertions.assertArrayEquals(t, t_prime);

        //Generating a (Schnorr/ECDHIES) key pair from passphrase pw:
        BigInteger s = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(pw, "".getBytes(), 512, "K".getBytes()))).multiply(BigInteger.valueOf(4));
        EdwardsPoint V = G.multiply(s);

        //Encrypting a byte array m under the (Schnorr/ECDHIES) public key V:
        byte[] kk = new byte[512/4];
        rand.nextBytes(kk);
        BigInteger k = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(kk)).multiply(BigInteger.valueOf(4));
        EdwardsPoint W = V.multiply(k);
        EdwardsPoint Z = G.multiply(k);
        ke_ka = SHA3.KMACXOF256(W.x.toByteArray(), "".getBytes(), 1024, "P".getBytes());
        System.arraycopy(ke_ka, 0, ke, 0, 512/8);
        System.arraycopy(ke_ka, 512/8, ka, 0, 512/8);
        c = Utils.xor2Lanes(SHA3.KMACXOF256(ke, "".getBytes(), m.length*8, "PKE".getBytes()), m);
        t = SHA3.KMACXOF256(ka, m, 512, "PKA".getBytes());

        //Decrypting a cryptogram (Z, c, t) under passphrase pw:
        s = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(pw, "".getBytes(), 512, "K".getBytes()))).multiply(BigInteger.valueOf(4));
        W = Z.multiply(s);
        ke_ka = SHA3.KMACXOF256(W.x.toByteArray(), "".getBytes(), 1024, "P".getBytes());
        System.arraycopy(ke_ka, 0, ke, 0, 512/8);
        System.arraycopy(ke_ka, 512/8, ka, 0, 512/8);
        d_m = Utils.xor2Lanes(SHA3.KMACXOF256(ke, "".getBytes(), c.length*8, "PKE".getBytes()), c);
        t_prime = SHA3.KMACXOF256(ka, d_m, 512, "PKA".getBytes());
        Utils.printBits(Utils.byteArrayToBitArray(t), "t");
        Utils.printBits(Utils.byteArrayToBitArray(t_prime), "t'");
        Assertions.assertArrayEquals(t, t_prime);

        //Generating a signature for a byte array m under passphrase pw:
        s = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(pw, "".getBytes(), 512, "K".getBytes()))).multiply(BigInteger.valueOf(4));
        k = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(s.toByteArray(), m, 512, "N".getBytes()))).multiply(BigInteger.valueOf(4));
        EdwardsPoint U = G.multiply(k);
        BigInteger hh = Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(U.x.toByteArray(), m, 512, "T".getBytes())));
        BigInteger zz = k.subtract(hh.multiply(s)).mod(EdwardsPoint.R);

        //Verifying a signature (h, z) for a byte array m under the (Schnorr/ ECDHIES) public key V:
        U = G.multiply(zz).add(V.multiply(hh));
        Assertions.assertEquals(Utils.bitsArrayToBigInteger(Utils.byteArrayToBitArray(SHA3.KMACXOF256(U.x.toByteArray(), m, 512, "T".getBytes()))), hh);
    }
}