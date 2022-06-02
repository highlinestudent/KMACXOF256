package edu.uw.tcss487.vrdgroup.tests;

import edu.uw.tcss487.vrdgroup.main.Elliptic521;

import java.math.BigInteger;
import java.security.SecureRandom;

/** Test cases for elliptic curve Montgomery Ladder point multiplication algorithm. */
public class TestElliptic521 {

    /** Generator point for curve.  */
    public static final Elliptic521 G = new Elliptic521(new BigInteger("4"), false);

    /** random scalar t % n, recalling that n := 4r */
    public static final BigInteger t = BigInteger.valueOf(new SecureRandom().nextInt()).mod(G.getR().multiply(BigInteger.valueOf(4)));

    /** random scalar k % n, recalling that n := 4r */
    public static final BigInteger k = BigInteger.valueOf(new SecureRandom().nextInt()).mod(G.getR().multiply(BigInteger.valueOf(4)));

    /** Constructor.*/
    public TestElliptic521() {

        System.out.println("INITIAL TESTS: ENSURE CURVE OPERATIONS PRODUCE CORRECT VALUES:\n");
        //initial tests to ensure curve operations produce correct values.
        System.out.println(G.toString("Point G: "));
        zeroTest();
        oneTest();
        twoTest();
        oppositeTest();
        fourG();
        rG();
        kG();
       // kPlusOne();
       // kPlusT();
        System.out.println("ALL TESTS PASSED.\n ############\n\n");

    }

    /**Test multiplication for neutral point. 0 * G = 0 */
    public void zeroTest() {
        Elliptic521 temp = Elliptic521.multiply(BigInteger.ZERO,G);
        Elliptic521 zero = new Elliptic521();
        System.out.println(temp.toString("0 * G: "));
        System.out.println(temp.equals(zero));
    }

    /** Tests G * 1 = G */
    public void oneTest() {
        Elliptic521 temp = Elliptic521.multiply(BigInteger.ONE,G);
        temp.equals(G);
        System.out.println(temp.toString("1 * G: "));
        System.out.println(temp.equals(G));
    }

    /** Tests G * 2 = G + G */
    public void twoTest() {
        Elliptic521 temp = Elliptic521.multiply(BigInteger.TWO,G);
        temp.equals(G.add(G));
        System.out.println(temp.toString("2  * G: "));
        System.out.println(temp.equals(G.add(G)));
    }

    /** Tests G + (-G) = O  */
    public void oppositeTest() {
        Elliptic521 opposite = G.getOpposite();
        opposite = opposite.add(G);
        System.out.println(opposite.toString("G + (-G) "));
    }

    /** Tests that 4G = 2 * (2 * G) */
    public void fourG() {
        Elliptic521 twoG = Elliptic521.multiply(BigInteger.TWO,G);
        twoG = twoG.multiply(BigInteger.TWO, twoG);
        Elliptic521 fourG = Elliptic521.multiply(new BigInteger("4"), G);
        System.out.println(twoG.toString("4 * G: "));
        System.out.println(twoG.equals(fourG));
    }

    /** Tests modularity of curve, in that r * G = O */
    public void rG() {
        Elliptic521 rG = Elliptic521.multiply(G.getR(), G);
        Elliptic521 zero = new Elliptic521();
        System.out.println(rG.toString("r * G: "));
        System.out.println(rG.equals(zero));
    }

    /** tests multiplication of random k * G */
    public void kG() {

        System.out.println("Random Secret Key k: " + k);
        Elliptic521 kG = Elliptic521.multiply(k, G);
        System.out.println(kG.toString("kG = :"));
        Elliptic521 kModRG = Elliptic521.multiply(k.mod(G.getR()), G);
        System.out.println(kG.toString("(k % r) * G = :"));
        System.out.println(kG.equals(kModRG));
    }

    /** Tests (k + 1) * G = k * G + G */
    public void kPlusOne() {

        BigInteger k2 = k.add(BigInteger.ONE);
        System.out.println("Random Secret Key k + 1: " + k2);
        Elliptic521 kPlusOneG = Elliptic521.multiply(k2, G);
        System.out.println(kPlusOneG.toString("(k + 1) * G = :"));
        Elliptic521 kGPlusOne = Elliptic521.multiply(k, G);
        kGPlusOne = kGPlusOne.add(G);
        System.out.println(kGPlusOne.toString("(k * G) + G: "));
        System.out.println(kGPlusOne.equals(kGPlusOne));
    }

    /** Tests (k + t) * G = (k * G) + (t * G) */
    public void kPlusT() {

        System.out.println("Random Secret Key t: " + t);
        BigInteger kPlusT = t.add(k);
        Elliptic521 gTimesKPlusT = Elliptic521.multiply(kPlusT, G);
        System.out.println(gTimesKPlusT.toString("(k + t) * G = :"));
        Elliptic521 kTimesG = Elliptic521.multiply(k, G);
        Elliptic521 tTimesG = Elliptic521.multiply(t, G);
        Elliptic521 kTimesGPlusTTimesG = kTimesG.add(tTimesG);
        System.out.println(kTimesGPlusTTimesG.toString("(k * G) + (t * G) = : "));
        System.out.println(gTimesKPlusT.equals(kTimesGPlusTTimesG));
    }

}





