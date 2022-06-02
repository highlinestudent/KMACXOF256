package edu.uw.tcss487.vrdgroup.main;

/**
 * @author Vu Nguyen
 * @version 0.0001
 * This class contains all methods related to Keccak1600
 */
public class Keccak1600 {
    /**
     * To implement SHA-3 we only care below values from KECCAK permutation
     */
    private static int b = 1600;
    public static int w = 64;
    private static int l = 6;

    /**
     * Map the splice of state arrays into convention from NIST.FIPS.202
     */
    private static int[] xy_map = new int[] {2, 3, 4, 0, 1};

    /**
     * Implement theta step, we do exactly the description in NIST.FIPS.202
     * @param A state bit array
     * @return
     */
    public static byte[][][] theta(byte[][][] A) {
        //printBytes(S, "Before Theta");

        //printState(A, "init state");

        byte[][] C = new byte[5][w];
        byte[][] D = new byte[5][w];

//        for (int x = 0; x < 5; x++) {
//            for (int y = 0; y < 5; y++) {
//                printBits(A[x][y], "lane " + x + " " + y);
//            }
//        }

//        for (int x = 0; x < 5; x ++) {
//            for (int y = 0; y < 5; y++) {
//                printBits(A[x][y], "lane " + x + " " + y);
//            }
//        }


        //Formula: A[x, y, z] = S[w(5y+x)+z]

        //1. For all pairs (x,z) such that 0≤x<5 and 0≤z<w, let
        //C[x,z]=A[x, 0,z] ⊕ A[x, 1,z] ⊕ A[x, 2,z] ⊕ A[x, 3,z] ⊕ A[x, 4,z].
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < w; z++) {
                C[x][z] = (byte) (A[x][0][z] ^ A[x][1][z] ^ A[x][2][z] ^ A[x][3][z] ^ A[x][4][z]);
                //System.out.println(C[x][z] + " = " + A[x][0][z] + " ^ " + A[x][1][z] + " ^ " + A[x][2][z] + " ^ " + A[x][3][z] + " ^ " + A[x][4][z]);
//                C[x][z] = (byte) (A[xy_map[x]][xy_map[0]][z]
//                                                ^ A[xy_map[x]][xy_map[1]][z]
//                                                ^ A[xy_map[x]][xy_map[2]][z]
//                                                ^ A[xy_map[x]][xy_map[3]][z]
//                                                ^ A[xy_map[x]][xy_map[4]][z]);
            }

            //printBits(C[x], "Xor C: " + x + " ");
        }

        //2. For all pairs (x, z) such that 0≤x<5 and 0≤z<w let
        //D[x,z]=C[(x-1) mod 5, z] ⊕ C[(x+1) mod 5, (z –1) mod w].
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < w; z++) {
                D[x][z] = (byte) (C[Utils.floorMod(x-1, 5)][z] ^ C[(x+1)%5][Utils.floorMod(z-1, w)]);
                //D[xy_map[x]][z] = (byte) (C[xy_map[floorMod(x-1, 5)]][z] ^ C[xy_map[(x+1)%5]][floorMod(z-1, w)]);
            }
            //printBits(D[x], "Xor D: " + x + " ");
        }

        //3. For all triples (x, y, z) such that 0≤x<5, 0≤y<5, and 0≤z<w, let
        //A′[x, y,z] = A[x, y,z] ⊕ D[x,z].
        byte[][][] AA = new byte[5][5][w];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < w; z++) {
                    AA[x][y][z] = (byte) (A[x][y][z] ^ D[x][z]);
                    //AA[xy_map[x]][xy_map[y]][z] = (byte) (A[xy_map[x]][xy_map[y]][z] ^ D[x][z]);
                }
                //printBits(AA[x][y], "lane " + x + " " + y);
            }
        }

        return AA;
    }

    /**
     * Implement theta step from https://keccak.team/files/Keccak-reference-3.0.pdf
     * @param A
     * @return
     */
    public static byte[][][] theta1(byte[][][] A) {
        byte[][] C = new byte[5][5];
        byte[][] D = new byte[5][5];
        byte[][][] AA = new byte[5][5][w];

        for (int x = 0; x < 5; x++) {
            C[x] = A[xy_map[x]][xy_map[0]];
            for (int y = 1; y < 5; y++) {
                C[x] = Utils.xor2Lanes(C[x], A[xy_map[x]][xy_map[y]]);
            }
        }
        for (int x = 0; x < 5; x++) {
            D[x] = Utils.xor2Lanes(C[Utils.floorMod(x - 1, 5)], Utils.ROT(C[(x + 1) % 5], 1));
            for (int y = 0; y < 5; y++) {
                AA[x][y] = Utils.xor2Lanes(A[xy_map[x]][xy_map[y]], D[x]);
            }
        }
        //printState(AA, "after theta");
        return AA;
    }

    /**
     * Implement roh step from https://keccak.team/files/Keccak-reference-3.0.pdf
     * @param A
     * @return
     */
    public static byte[][][] roh1(byte[][][] A) {
        byte[][][] AA = new byte[5][5][w];
        int x = 1;
        int y = 0;
        for (int t = 0; t < 24; t++) {
            AA[x][y] = Utils.ROT(A[x][y], (t+1)*(t+2)/2);
            int tmpX = x;
            x = y;
            y = (2*tmpX+3*y)%5;
        }
        return AA;
    }

    /**
     * Implement the roh step, we do exactly the description in NIST.FIPS.202
     * @param A state bit array
     * @return
     */
    public static byte[][][] roh(byte[][][] A) {
        byte[][][] AA = new byte[5][5][w];

        //Formula: A[x, y, z] = S[w(5y+x)+z]

        //1. For all z such that 0≤z<w, let A′ [0, 0,z] = A[0, 0,z]
        for (int z = 0; z < w; z++) {
            AA[0][0][z] = A[0][0][z];
        }

        //2. Let (x, y) = (1, 0).
        int x = 1, y = 0;

        //3. For t from 0 to 23:
        //a. for all z such that 0≤z<w, let A′[x, y,z] = A[x, y, (z–(t+1)(t+2)/2) mod w];
        //b. let (x, y) = (y, (2x+3y) mod 5).
        for (int t = 0; t <= 23; t++) {
            for (int z = 0; z < w; z++) {
                //SS[w * (5 * y + x) + i] = S[w*(5*y+x)+((i-(t+1)*(t+2)/2)%w)];
                //System.out.println(w + " " + z + " " + x + " " + y + " " + t + " " + floorMod(z-(t+1)*(t+2)/2, w) + " " + (z-(t+1)*(t+2)/2));
                AA[x][y][z] = A[x][y][Utils.floorMod(z-(t+1)*(t+2)/2, w)];
            }
            int tempX = x;
            x = y;
            y = (2*tempX+3*y)%5;
        }

        return AA;
    }

    /**
     * Implement the pi step, we do exactly the description in NIST.FIPS.202
     * @param A state bit array
     * @return
     */
    public static byte[][][] pi(byte[][][] A) {
        byte[][][] AA = new byte[5][5][w];

        //Formula: A[x, y, z] = S[w(5y+x)+z]

        //1. For all triples (x, y, z) such that 0≤x<5, 0≤y<5, and 0≤z<w, let
        //A′[x, y, z]=A[(x + 3y) mod 5, x, z].
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < w; z++) {
                    //setBitAt(i, j, k, SS, getBitAt((i+3*j)%5, i, k, S));
                    AA[x][y][z] = A[(x + 3*y)%5][x][z];
                }
            }
        }

        return AA;
    }

    /**
     * Implement the chi step, we do exactly the description in NIST.FIPS.202
     * @param A state bit array
     * @return
     */
    public static byte[][][] chi(byte[][][] A) {
        byte[][][] AA = new byte[5][5][w];

        //Formula: A[x, y, z] = S[w(5y+x)+z]

        //For all triples (x, y, z) such that 0≤x<5, 0≤y<5, and 0≤z<w, let
        //A′[x, y,z] = A[x, y,z] ⊕ ((A[(x+1) mod 5, y, z] ⊕ 1) ⋅ A[(x+2) mod 5, y, z]).
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < w; z++) {
                    //setBitAt(i, j, k, SS, (byte) (getBitAt(i, j, k, S) ^ ((getBitAt((i+1)%5, j, k, S) ^ 0x01) & getBitAt((i+2)%5, j, k, S))));
                    AA[x][y][z] = (byte) (A[x][y][z] ^ ((A[(x + 1) % 5][y][z] ^ 1) & A[(x + 2) % 5][y][z]));
                }
            }
        }
        return AA;
    }

    /**
     * Implement the iota step, we do exactly the description in NIST.FIPS.202
     * @param A state bit array
     * @param i_r
     * @return
     */
    public  static byte[][][] iota(byte[][][] A, int i_r) {
        byte[][][] AA = new byte[5][5][w];

        //1. For all triples (x, y,z) such that 0≤x<5, 0≤y<5, and 0≤z<w, let A′[x, y,z] = A[x, y,z].
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < w; z++) {
                    AA[x][y][z] = A[x][y][z];
                }
            }
        }
        //2. Let RC=0
        byte[] RC = new byte[w];
        //3. For j from 0 to l, let RC[2^j–1]=rc(j+7*i_r).
        for (int j = 0; j <= l; j++) {
            RC[(int)(Math.pow(2, j) - 1)] = rc(j + 7*i_r);
        }
        //printBits(RC, "RC");
        //4. For all z such that 0≤z<w, let A′[0, 0,z]=A′[0, 0,z] ⊕ RC[z].
        for (int z = 0; z < w; z++) {
            AA[0][0][z] = (byte) (AA[0][0][z] ^ RC[z]);
        }

        return AA;
    }

    /**
     * Round function, we do exactly the description in NIST.FIPS.202
     * @param A state bit array
     * @param i_r
     * @return
     */
    public static byte[][][] Rnd(byte[][][] A, int i_r) {
        return iota(chi(pi(roh(theta(A)))), i_r);
    }

    /**
     * KECCAK-p[b, n_r](S), we fixed b = 1600, so we don't need this parameter in the function
     * @param n_r
     * @param S bits array, not byte
     * @return
     */
    public static byte[] KECCAK_p(int n_r, byte[] S) {
        //1. Convert S into a state array, A, as described in Sec. 3.1.2.
        byte[][][] A = bitArrayToStateArrays(S);
        //2. For i_r from 12+2l–n_r to 12+2l–1, let A=Rnd(A, i_r)
        for (int i_r = 12+2*l-n_r; i_r <= 12+2*l-1; i_r++) {
            //System.out.println("Round #" + i_r);
            A = Rnd(A, i_r);
            //printBits(stateArraysToBitArray(A), "round " + i_r);
        }
        //3. Convert A into a string S′ of length b, as described in Sec. 3.1.3
        //4. Return S′
        return stateArraysToBitArray(A);
    }

    /**
     * Convert state arrays to an bit array
     * @param A
     * @return
     */
    public static byte[] stateArraysToBitArray(byte[][][] A) {
        byte[][][] lane = new byte[5][5][w];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < w; k++) {
                    lane[i][j][k] = A[i][j][k];
                }
            }
        }

        byte[][] plane = new byte[5][w*5];
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                System.arraycopy(lane[i][j], 0, plane[j], i*w, w);
                //printBits(lane[i][j], "lane " + i + " " + j);
            }
            //printBits(plane[j], "Plane " + j);
        }

        byte[] S = new byte[w*5*5];
        System.arraycopy(plane[0], 0, S, 0, w*5);
        System.arraycopy(plane[1], 0, S, w*5, w*5);
        System.arraycopy(plane[2], 0, S, 2*w*5, w*5);
        System.arraycopy(plane[3], 0, S, 3*w*5, w*5);
        System.arraycopy(plane[4], 0, S, 4*w*5, w*5);

        //printBytes(S, "test");

        return S;
    }

    /**
     * Convert bit array to state arrays
     * @param S
     * @return
     */
    public static byte[][][] bitArrayToStateArrays(byte[] S) {
        byte[][][] A = new byte[5][5][w];

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < w; z++) {
                    //Formula: A[x, y, z] = S[w(5y+x)+z]
                    A[x][y][z] = S[w*(5*y+x)+z];
                }
//                byte[] tmp = new byte[w];
//                for (int l = 0; l < w; l+=8) {
//                    System.arraycopy(A[x][y], w - (l + 8), tmp, l, 8);
//                }
//                A[x][y] = tmp;

                //printBits(A[x][y], "lane " + x + " " + y);
            }
        }
        return A;
    }

    /**
     * KECCAK[c] (N, d) = SPONGE[KECCAK-p[1600, 24], pad10*1, 1600–c] (N, d).
     * @param c
     * @param N bit array, not byte
     * @param d
     * @return
     */
    public static byte[] KECCAK(int c, byte[] N, int d) {
        return SPONGE(1600 - c, N, d);
    }

    /**
     * The original function:
     * Algorithm 8: SPONGE[f, pad, r](N, d), but for Keccak[c]:
     * we knew f = Keccak-p(1600, 24) or in our code, it is Keccak-p(24)
     * and pad is pad10*1
     * @param r
     * @param N bit array, not byte
     * @param d
     * @return
     */
    public static byte[] SPONGE(int r, byte[] N, int d) {
        //1. Let P=N || pad(r, len(N)).
        byte[] padding = pad10_1(r, N.length);
        byte[] P = new byte[N.length + padding.length];
        System.arraycopy(N, 0, P, 0, N.length);
        System.arraycopy(padding, 0, P, N.length, padding.length);

        //printBytes(P, "after padding");

        //2. Let n=len(P)/r.
        int n = P.length / r;

        //3. Let c=b-r.
        int c = b - r;

        //4. Let P0, … , P_n-1 be the unique sequence of strings of length r such that P = P0 || … || P_n-1.
        //do nothing

        //5. Let S=0^b
        byte[] S = new byte[b];

        //6. For i from 0 to n-1, let S=f(S ⊕ (Pi || 0^c))
        byte[] tmp = new byte[r + c];
        byte[] zero_c = new byte[c];
        for (int i = 0; i <= n - 1; i++) {
            System.arraycopy(P, i*r, tmp, 0, r);
            System.arraycopy(zero_c, 0, tmp, r, zero_c.length);

            for (int j = 0; j < tmp.length; j++) {
                tmp[j] = (byte) (tmp[j] ^ S[j]);
            }

            //printBytes(tmp, "after xor");

            S = KECCAK_p(24, tmp);
        }

        //7. Let Z be the empty string.
        byte[] Z = null;
        //8. Let Z=Z || Trunc_r(S).
        //9. If d≤|Z|, then return Trunc_d(Z); else continue.
        //10. Let S=f(S), and continue with Step 8.
        do {
            byte[] truncS = trunc(r, S);
            if (Z == null) {
                Z = truncS;
            } else {
                byte[] newZ = new byte[Z.length + truncS.length];
                System.arraycopy(Z, 0, newZ, 0, Z.length);
                System.arraycopy(truncS, 0, newZ, Z.length, truncS.length);
                Z = newZ;
            }

            if (d <= Z.length) {
                return trunc(d, Z);
            }
            S = KECCAK_p(24, S);
        } while (true);
    }

    /**
     * Algorithm 9: pad10*1(x, m)
     * @param x
     * @param m
     * @return
     */
    public static byte[] pad10_1(int x, int m) {
        //1. Let j = (– m – 2) mod x.
        int j = Utils.floorMod(-m-2, x);

        //2. Return P = 1 || 0^j || 1
        byte[] P = new byte[j+2];
        for (int i = 0; i < P.length; i++) {
            P[i] = 0x00;
        }
        P[0] = 1;
        P[P.length - 1] = 1;

        //printBytes(P, "Padding");

        return P;
    }

    /**
     * Algorithm 5: rc(t)
     * Input:
     * integer t.
     * Output:
     * bit rc(t).
     *
     * @param t
     * @return a byte represent a bit (be careful)
     */
    public static byte rc(int t) {
        //1. If t mod 255 = 0, return 1.
        if (t % 255 == 0) return 0x01;

        //2. Let R = 10000000.
        byte[] R = new byte[]{1, 0, 0, 0, 0, 0, 0, 0};

        //3. For i from 1 to t mod 255, let:
        //a. R = 0 || R;
        //b. R[0] = R[0] ⊕ R[8];
        //c. R[4] = R[4] ⊕ R[8];
        //d. R[5] = R[5] ⊕ R[8];
        //e. R[6] = R[6] ⊕ R[8];
        //f. R =Trunc8[R].
        for (int i = 1; i <= t % 255; i++) {
            //a
            byte[] RR = new byte[R.length + 1];
            System.arraycopy(R, 0, RR, 1, R.length);
            R[0] = 0;
            R = RR;
            //b
            R[0] = (byte) (R[0] ^ R[8]);
            //c
            R[4] = (byte) (R[4] ^ R[8]);
            //d
            R[5] = (byte) (R[5] ^ R[8]);
            //e
            R[4] = (byte) (R[6] ^ R[8]);
            //f
            R = trunc(8, R);
        }

        //4. Return R[0].
        return R[0];
    }

    /**
     * Return first r bits
     * @param r
     * @param S bit array, not byte
     * @return
     */
    public static byte[] trunc(int r, byte[] S) {
        byte[] truncS = new byte[r];
        System.arraycopy(S, 0, truncS, 0, r);

        return truncS;
    }
}
