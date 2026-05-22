package com.telo.app.otp;

import java.util.HashMap;
import java.util.Map;

public class OTPIconResolver {

    private static final Map<String, String> ICON_MAP = new HashMap<>();

    static {
        ICON_MAP.put("google",    "ic_google");
        ICON_MAP.put("github",    "ic_github");
        ICON_MAP.put("facebook",  "ic_facebook");
        ICON_MAP.put("twitter",   "ic_twitter");
        ICON_MAP.put("microsoft", "ic_microsoft");
        ICON_MAP.put("apple",     "ic_apple");
        ICON_MAP.put("amazon",    "ic_amazon");
        ICON_MAP.put("discord",   "ic_discord");
        ICON_MAP.put("instagram", "ic_instagram");
        ICON_MAP.put("steam",     "ic_steam");
        ICON_MAP.put("dropbox",   "ic_dropbox");
        ICON_MAP.put("paypal",    "ic_paypal");
        ICON_MAP.put("binance",   "ic_binance");
        ICON_MAP.put("coinbase",  "ic_coinbase");
        ICON_MAP.put("netflix",   "ic_netflix");
        ICON_MAP.put("linkedin",  "ic_linkedin");
        ICON_MAP.put("reddit",    "ic_reddit");
        ICON_MAP.put("twitch",    "ic_twitch");
        ICON_MAP.put("yahoo",     "ic_yahoo");
        ICON_MAP.put("gitlab",    "ic_gitlab");
    }

    public static String resolve(String issuer, String name) {
        if (issuer != null) {
            String key = issuer.toLowerCase().trim();
            for (Map.Entry<String, String> entry : ICON_MAP.entrySet()) {
                if (key.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        if (name != null) {
            String key = name.toLowerCase().trim();
            for (Map.Entry<String, String> entry : ICON_MAP.entrySet()) {
                if (key.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return "ic_otp_default";
    }

    public static String getFirstLetter(String issuer, String name) {
        if (issuer != null && !issuer.isEmpty()) {
            return String.valueOf(issuer.charAt(0)).toUpperCase();
        }
        if (name != null && !name.isEmpty()) {
            return String.valueOf(name.charAt(0)).toUpperCase();
        }
        return "?";
    }
}