package com.telo.app.vault;

public class PasswordSlot extends Slot {

    private byte[] salt;
    private int    iterations;
    private int    memoryKb;
    private int    parallelism;

    public PasswordSlot() {
        super(Type.PASSWORD);
        // Scrypt default params
        this.iterations  = 32768;
        this.memoryKb    = 32768;
        this.parallelism = 1;
    }

    public byte[] getSalt()       { return salt; }
    public int getIterations()    { return iterations; }
    public int getMemoryKb()      { return memoryKb; }
    public int getParallelism()   { return parallelism; }

    public void setSalt(byte[] salt)           { this.salt = salt; }
    public void setIterations(int iterations)  { this.iterations = iterations; }
    public void setMemoryKb(int memoryKb)      { this.memoryKb = memoryKb; }
    public void setParallelism(int p)          { this.parallelism = p; }
}