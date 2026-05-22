package com.telo.app.vault;

public class ScryptParams {

    private final byte[] salt;
    private final int    n;  // CPU/memory cost
    private final int    r;  // Block size
    private final int    p;  // Parallelization

    public static final int DEFAULT_N = 32768;
    public static final int DEFAULT_R = 8;
    public static final int DEFAULT_P = 1;

    public ScryptParams(byte[] salt, int n, int r, int p) {
        this.salt = salt;
        this.n    = n;
        this.r    = r;
        this.p    = p;
    }

    public static ScryptParams createDefault(byte[] salt) {
        return new ScryptParams(salt, DEFAULT_N, DEFAULT_R, DEFAULT_P);
    }

    public byte[] getSalt() { return salt; }
    public int getN()       { return n; }
    public int getR()       { return r; }
    public int getP()       { return p; }
}