package edu.uw.tcss487.vrdgroup.tests;

import edu.uw.tcss487.vrdgroup.main.EdwardsPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Rin Pham
 * Unit tests for elliptic curve
 */
class EdwardsPointTest {

    @Test
    void multiply() {
        Random rand = new Random();
        EdwardsPoint G = new EdwardsPoint(new BigInteger("4"), false);
        EdwardsPoint O = new EdwardsPoint(BigInteger.ZERO, BigInteger.ONE);

        EdwardsPoint G_times_0 = G.multiply(BigInteger.ZERO);
        EdwardsPoint G_times_1 = G.multiply(BigInteger.ONE);
        EdwardsPoint G_times_2 = G.multiply(BigInteger.valueOf(2));
        EdwardsPoint G_times_4 = G.multiply(BigInteger.valueOf(4));
        EdwardsPoint G_adds_negate_G = G.add(G.negate());
        EdwardsPoint G_adds_G = G.add(G);
        EdwardsPoint G_times_2_times_2 = G.multiply(BigInteger.valueOf(2)).multiply(BigInteger.valueOf(2));
        EdwardsPoint G_times_r = G.multiply(EdwardsPoint.R);

        Assertions.assertEquals(O, G_times_0);
        Assertions.assertEquals(G, G_times_1);
        Assertions.assertEquals(O, G_adds_negate_G);
        Assertions.assertEquals(G_times_2, G_adds_G);
        Assertions.assertEquals(G_times_4, G_times_2_times_2);
        Assertions.assertNotEquals(G_times_4, O);
        Assertions.assertEquals(G_times_r, O);

        int maxRandom = 10000000;
        int maxRun = 10;
        for (int i = 0; i < maxRun; i++) {
            BigInteger k = BigInteger.valueOf(rand.nextInt(maxRandom));
            BigInteger t = BigInteger.valueOf(rand.nextInt(maxRandom));
            EdwardsPoint G_times_k = G.multiply(k);
            EdwardsPoint G_times_k_mod_r = G.multiply(k.mod(EdwardsPoint.R));
            EdwardsPoint G_times_k_plus_1 = G.multiply(k.add(BigInteger.ONE));
            EdwardsPoint G_times_k_plus_t = G.multiply(k.add(t));
            EdwardsPoint G_times_t = G.multiply(t);
            EdwardsPoint G_times_t_times_k = G.multiply(t).multiply(k);
            EdwardsPoint G_times_k_times_t = G.multiply(k).multiply(t);
            EdwardsPoint G_times_k_times_t_mod_r = G.multiply(k.multiply(t).mod(EdwardsPoint.R));

            Assertions.assertEquals(G_times_k, G_times_k_mod_r);
            Assertions.assertEquals(G_times_k_plus_1, G_times_k.add(G));
            Assertions.assertEquals(G_times_k_plus_t, G_times_k.add(G_times_t));
            Assertions.assertEquals(G_times_t_times_k, G_times_k_times_t);
            Assertions.assertEquals(G_times_k_times_t, G_times_k_times_t_mod_r);
        }
    }
}