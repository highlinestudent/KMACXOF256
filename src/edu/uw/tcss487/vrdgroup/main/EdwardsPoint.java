package edu.uw.tcss487.vrdgroup.main;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Rin Pham
 * @version 0.0001
 * Implementation E521 Elliptic curve
 */
public class EdwardsPoint implements Serializable {

    static final long serialVersionUID = 42L;

    public static BigInteger P = BigInteger.valueOf(2).pow(521).subtract(BigInteger.ONE);
    public static BigInteger D = (new BigInteger("376014")).negate();
    public static BigInteger R = BigInteger.valueOf(2).pow(519).subtract(new BigInteger("337554763258501705789107630418782636071904961214051226618635150085779108655765"));

    public BigInteger x;
    public BigInteger y;


    /**
     * Construct neutral point
     */
    public EdwardsPoint() {
        x = BigInteger.valueOf(0);
        y = BigInteger.valueOf(1);
    }

    /**
     * Construct point with x, y
     * @param x
     * @param y
     */
    public EdwardsPoint(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct point with x and the least significant bit of y
     * @param x
     * @param lsbitY
     */
    public EdwardsPoint(BigInteger x, boolean lsbitY) {
        this.x = x;

        BigInteger x2 = x.pow(2);
        BigInteger numerator = BigInteger.ONE.subtract(x2);
        BigInteger denominator = BigInteger.ONE.subtract(D.multiply(x2));
        BigInteger n = numerator.multiply(denominator.modInverse(P));

        this.y = sqrt(n, P, lsbitY);
    }

    /**
     * Check if two points are equal
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EdwardsPoint)) return false;
        EdwardsPoint otherP = (EdwardsPoint) object;
        return x.equals(otherP.x) && y.equals(otherP.y);
    }

    /**
     * Calculate oposite point
     * @return
     */
    public EdwardsPoint negate() {
        //return new EdwardsPoint(x, P.subtract(y));
        return new EdwardsPoint(x.negate(), y);
    }

    /**
     * Calculate sum of two points
     * @param point
     * @return
     */
    public EdwardsPoint add(EdwardsPoint point) {
        BigInteger one = BigInteger.ONE;
        BigInteger x1 = x, y1 = y, x2 = point.x, y2 = point.y;

        BigInteger numeratorX = x1.multiply(y2).add(y1.multiply(x2));
        BigInteger denominatorX = one.add(D.multiply(x1).multiply(x2).multiply(y1).multiply(y2));
        BigInteger x = numeratorX.multiply(denominatorX.modInverse(P)).mod(P);

        BigInteger numeratorY = y1.multiply(y2).subtract(x1.multiply(x2));
        BigInteger denominatorY = one.subtract(D.multiply(x1).multiply(x2).multiply(y1).multiply(y2));
        BigInteger y = numeratorY.multiply(denominatorY.modInverse(P)).mod(P);
        return new EdwardsPoint(x, y);
    }

    /**
     * Multiply by scalar
     * @param scalar
     * @return
     */
    public EdwardsPoint multiply(BigInteger scalar) {
        if (scalar.compareTo(BigInteger.ZERO) == 0) return new EdwardsPoint(BigInteger.ZERO, BigInteger.ONE);

        char[] bits = scalar.toString(2).toCharArray();
        EdwardsPoint P = new EdwardsPoint(this.x, this.y);
        EdwardsPoint V = P;
        for (int i = 1; i < bits.length; i++) {
            V = V.add(V);
            if (bits[i] == '1') {
                V = V.add(P);
            }
        }
        return V;
    }

    /**
     * Compute a square root of v mod p with a specified
     * least significant bit, if such a root exists. *
     * @param v the radicand.
     * @param p the modulus (must satisfy p mod 4 = 3).
     * @param lsb desired least significant bit (true: 1, false: 0).
     * @return a square rootr of v mod p with r mod 2 = 1 iff lsb=true
     *          if such a root exists, otherwise null.
     */
    public static BigInteger sqrt(BigInteger v, BigInteger p, boolean lsb) {
        assert (p.testBit(0) && p.testBit(1)); // p = 3 (mod 4)
        if (v.signum() == 0) {
            return BigInteger.ZERO;
        }
        BigInteger r = v.modPow(p.shiftRight(2).add(BigInteger.ONE), p);
        if (r.testBit(0) != lsb) {
            r = p.subtract(r); // correct the lsb
        }
        return (r.multiply(r).subtract(v).mod(p).signum() == 0) ? r : null; }
}
