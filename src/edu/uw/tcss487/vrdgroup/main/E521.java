package edu.uw.tcss487.vrdgroup.main;
import java.math.BigInteger;

/**
 * the E521 curve (Edwards curve)
 */
public class E521 {

    private BigInteger myX;
    private BigInteger myY;

    /** p parameter */
    private static final BigInteger  p  = (new BigInteger("2").pow(521)).subtract(BigInteger.ONE);
    /** d = =−376014 */
    private static final BigInteger d = new BigInteger("-376014");
    /** number of points on Curve.E521 Curve -> n := 4 * (r) .*/
    public static final BigInteger R = (new BigInteger("2").pow(519))
            .subtract(new BigInteger(
                    "337554763258501705789107630418782636071904961214051226618635150085779108655765"));


    /**
     * Constructor for point with arbitrary x and y.
     * @param theX x coordinate of point
     * @param theY y coordinate of point
     */
    public E521(final BigInteger theX, final BigInteger theY) {
        myX = theX;
        myY = theY;
    }

    /** Constructor for neutral element, defined to be point (0, 1) */
    public E521() {
        myX = BigInteger.ZERO;
        myY = BigInteger.ONE;
    }





    /**
     * Constructor for point with only X provided. Solves for Y.
     * @param theX int value which is desired X coordinate of point. Y is generated
     * from X using sqrt method.
     */
    public E521(final BigInteger theX, boolean theLSB) {
        myX = theX;
        //solve for y
        BigInteger num = (BigInteger.ONE.subtract(theX.pow(2))).mod(p);
        BigInteger denom = BigInteger.ONE.add(new BigInteger("376014").multiply(theX.pow(2))).mod(p);
        denom = denom.modInverse(p);
        BigInteger radicand = num.multiply(denom);
        myY = sqrt(radicand, p, theLSB);
    }

    /**
     * Gets current X value.
     * @return BigInteger which is current X coordinate of point.
     */
    public BigInteger getX() {
        return myX;
    }

    /** Set X coordinate. */
    public void setX(final BigInteger theX) {myX = theX;}
    /** Set Y coordinate. */
    public void setY(final BigInteger theY) {myY = theY;}

    /**
     * Gets current Y value.
     * @return BigInteger which is current Y coordinate of point.
     */
    public BigInteger getY() { return myY; }

    /** Returns r value for curve.
     * @return r value for curve. */
    public BigInteger getR() { return R; }

    /**
     * Compute a square root of v mod p with a specified
     * least significant bit, if such a root exists.
     *
     * @param v the radicand.
     * @param p the modulus (must satisfy p mod 4 = 3).
     * @param lsb desired least significant bit (true: 1, false: 0).
     * @return a square root r of v mod p with r mod 2 = 1 iff lsb = true
     * if such a root exists, otherwise null.
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
        return (r.multiply(r).subtract(v).mod(p).signum() == 0) ? r : null;
    }

    /**
     * Gets the opposite value of a point
     * @return the opposite value of the given point.
     */
    public E521 getOpposite() {

        return new E521(myX.negate(), this.getY());
    }

    /**
     * Sum of two points based on the Edwards point addition formula.
     * @param theOther the point is added
     * @return a point that sum of 2 given points
     */
    public E521 add(final E521 theOther) {
        BigInteger x1 = this.getX();
        BigInteger x2 = theOther.getX();

        BigInteger y1 = this.getY();
        BigInteger y2 = theOther.getY();

        BigInteger xNum = ((x1.multiply(y2)).add(y1.multiply(x2))).mod(p);
        BigInteger xDenom = (BigInteger.ONE.add(d.multiply(x1).multiply(x2).multiply(y1).multiply(y2))).mod(p);

        xDenom = xDenom.modInverse(p);
        BigInteger newX = xNum.multiply(xDenom).mod(p);

        BigInteger yNum = (y1.multiply(y2).subtract(x1.multiply(x2))).mod(p);
        BigInteger yDenom = (BigInteger.ONE.subtract(d.multiply(x1).multiply(x2).multiply(y1).multiply(y2))).mod(p);

        yDenom = yDenom.modInverse(p);
        BigInteger newY = yNum.multiply(yDenom).mod(p);

        return new E521(newX, newY);
    }

    /**
     * Computes k*P as P[1] + P[2] ... P[k]
     * @param k is number of times to add P to itself.
     * @param P is the point to multiply.
     */
    public E521 multiply(final BigInteger k, final E521 P) {
        E521 mulP = new E521(BigInteger.ZERO, BigInteger.ONE);
        for(int i = 0; i < k.intValue(); i ++) {
            mulP = mulP.add(P);
        }
        return mulP;
    }



    /**
     * Gets string representation of object as pair of X and Y coordinates.
     * @return String which is labelled pair of X and Y coordinates.
     */
    public String toString(final String name) {
        return name + "\n" + "X coordinate: " + getX().toString() + "\n"
                + "Y coordinate: " + getY().toString() + "\n";
    }


}
