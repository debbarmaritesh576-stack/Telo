package com.telo.app.security;

import com.telo.app.passwords.PasswordEntry;
import java.util.List;

public class SecurityReport {

    private SecurityScore           score;
    private List<PasswordEntry>     weakPasswords;
    private List<PasswordEntry>     expiredPasswords;
    private List<PasswordEntry>     expiringSoonPasswords;
    private List<List<PasswordEntry>> duplicatePasswords;
    private List<PasswordEntry>     breachedPasswords;
    private int                     totalPasswords;
    private int                     totalOTP;
    private long                    generatedAt;

    public SecurityReport() {
        this.generatedAt = System.currentTimeMillis();
    }

    // ── Getters ───────────────────────────────────────────────

    public SecurityScore              getScore()               { return score; }
    public List<PasswordEntry>        getWeakPasswords()       { return weakPasswords; }
    public List<PasswordEntry>        getExpiredPasswords()    { return expiredPasswords; }
    public List<PasswordEntry>        getExpiringSoon()        { return expiringSoonPasswords; }
    public List<List<PasswordEntry>>  getDuplicatePasswords()  { return duplicatePasswords; }
    public List<PasswordEntry>        getBreachedPasswords()   { return breachedPasswords; }
    public int                        getTotalPasswords()      { return totalPasswords; }
    public int                        getTotalOTP()            { return totalOTP; }
    public long                       getGeneratedAt()         { return generatedAt; }

    // ── Setters ───────────────────────────────────────────────

    public void setScore(SecurityScore score)                              { this.score = score; }
    public void setWeakPasswords(List<PasswordEntry> list)                 { this.weakPasswords = list; }
    public void setExpiredPasswords(List<PasswordEntry> list)              { this.expiredPasswords = list; }
    public void setExpiringSoon(List<PasswordEntry> list)                  { this.expiringSoonPasswords = list; }
    public void setDuplicatePasswords(List<List<PasswordEntry>> list)      { this.duplicatePasswords = list; }
    public void setBreachedPasswords(List<PasswordEntry> list)             { this.breachedPasswords = list; }
    public void setTotalPasswords(int count)                               { this.totalPasswords = count; }
    public void setTotalOTP(int count)                                     { this.totalOTP = count; }

    // ── Summary ───────────────────────────────────────────────

    public int getTotalIssues() {
        int issues = 0;
        if (weakPasswords     != null) issues += weakPasswords.size();
        if (expiredPasswords  != null) issues += expiredPasswords.size();
        if (duplicatePasswords != null) issues += duplicatePasswords.size();
        if (breachedPasswords != null) issues += breachedPasswords.size();
        return issues;
    }

    public boolean hasIssues() {
        return getTotalIssues() > 0;
    }
}