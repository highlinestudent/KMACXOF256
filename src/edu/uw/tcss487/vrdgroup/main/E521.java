package edu.uw.tcss487.vrdgroup.main;

import java.math.BigInteger;

/**
 * the E521 curve (Edwards curve)
 */
public class E521 {

    private BigInteger myX;
    private BigInteger myY;

    private static final BigInteger  P  = new BigInteger("2").pow(521) - BigInteger.ONE;
    private static final BigInteger D = new BigInteger("-376014");


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

}
