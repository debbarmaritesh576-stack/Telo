package com.telo.app.autofill;

public class AutofillDataset {

    private String username;
    private String email;
    private String password;
    private String label;
    private String iconRes;

    public AutofillDataset() {}

    public AutofillDataset(
            String label,
            String username,
            String email,
            String password,
            String iconRes) {
        this.label    = label;
        this.username = username;
        this.email    = email;
        this.password = password;
        this.iconRes  = iconRes;
    }

    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getLabel()    { return label; }
    public String getIconRes()  { return iconRes; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setLabel(String label)       { this.label = label; }
    public void setIconRes(String iconRes)   { this.iconRes = iconRes; }
}