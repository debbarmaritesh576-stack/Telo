package com.telo.app.security;

public class SecurityScore {

    public enum Level {
        CRITICAL,   // 0-20
        POOR,       // 21-40
        FAIR,       // 41-60
        GOOD,       // 61-80
        EXCELLENT   // 81-100
    }

    private int    totalScore;
    private int    passwordScore;
    private int    otpScore;
    private int    backupScore;
    private int    vaultScore;
    private Level  level;
    private String summary;

    public SecurityScore() {
        this.totalScore    = 0;
        this.passwordScore = 0;
        this.otpScore      = 0;
        this.backupScore   = 0;
        this.vaultScore    = 0;
    }

    public void calculate() {
        totalScore = (passwordScore + otpScore +
                      backupScore + vaultScore) / 4;
        totalScore = Math.min(100, Math.max(0, totalScore));

        if (totalScore <= 20)      level = Level.CRITICAL;
        else if (totalScore <= 40) level = Level.POOR;
        else if (totalScore <= 60) level = Level.FAIR;
        else if (totalScore <= 80) level = Level.GOOD;
        else                       level = Level.EXCELLENT;

        summary = buildSummary();
    }

    private String buildSummary() {
        switch (level) {
            case CRITICAL:  return "Your account security is critically weak!";
            case POOR:      return "Several security issues need attention";
            case FAIR:      return "Security is moderate — room to improve";
            case GOOD:      return "Good security — a few things to fix";
            case EXCELLENT: return "Excellent security setup!";
            default:        return "";
        }
    }

    public int    getTotalScore()    { return totalScore; }
    public int    getPasswordScore() { return passwordScore; }
    public int    getOtpScore()      { return otpScore; }
    public int    getBackupScore()   { return backupScore; }
    public int    getVaultScore()    { return vaultScore; }
    public Level  getLevel()         { return level; }
    public String getSummary()       { return summary; }

    public void setPasswordScore(int score) { this.passwordScore = score; }
    public void setOtpScore(int score)      { this.otpScore = score; }
    public void setBackupScore(int score)   { this.backupScore = score; }
    public void setVaultScore(int score)    { this.vaultScore = score; }
}