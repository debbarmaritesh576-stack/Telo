package com.telo.app.autofill;

import android.app.assist.AssistStructure;
import android.view.autofill.AutofillId;
import java.util.ArrayList;
import java.util.List;

public class AutofillParser {

    public static class ParsedStructure {
        public AutofillId usernameId;
        public AutofillId emailId;
        public AutofillId passwordId;
        public String     packageName;
        public String     webDomain;
    }

    public static ParsedStructure parse(AssistStructure structure) {
        ParsedStructure result = new ParsedStructure();
        int nodeCount = structure.getWindowNodeCount();
        for (int i = 0; i < nodeCount; i++) {
            AssistStructure.WindowNode node = structure.getWindowNodeAt(i);
            parseNode(node.getRootViewNode(), result);
        }
        return result;
    }

    private static void parseNode(
            AssistStructure.ViewNode node,
            ParsedStructure result) {

        String hint     = node.getHint();
        String idEntry  = node.getIdEntry();
        String[] hints  = node.getAutofillHints();

        if (hints != null) {
            for (String h : hints) {
                if (isUsernameHint(h) && result.usernameId == null) {
                    result.usernameId = node.getAutofillId();
                } else if (isEmailHint(h) && result.emailId == null) {
                    result.emailId = node.getAutofillId();
                } else if (isPasswordHint(h) && result.passwordId == null) {
                    result.passwordId = node.getAutofillId();
                }
            }
        }

        // Fallback to hint text
        if (hint != null) {
            String lowerHint = hint.toLowerCase();
            if (isUsernameHint(lowerHint) && result.usernameId == null) {
                result.usernameId = node.getAutofillId();
            } else if (isEmailHint(lowerHint) && result.emailId == null) {
                result.emailId = node.getAutofillId();
            } else if (isPasswordHint(lowerHint) && result.passwordId == null) {
                result.passwordId = node.getAutofillId();
            }
        }

        // Web domain
        if (node.getWebDomain() != null) {
            result.webDomain = node.getWebDomain();
        }

        // Recurse children
        for (int i = 0; i < node.getChildCount(); i++) {
            parseNode(node.getChildAt(i), result);
        }
    }

    private static boolean isUsernameHint(String hint) {
        return hint.contains("username") ||
               hint.contains("user") ||
               hint.contains("login") ||
               hint.contains("name");
    }

    private static boolean isEmailHint(String hint) {
        return hint.contains("email") ||
               hint.contains("e-mail");
    }

    private static boolean isPasswordHint(String hint) {
        return hint.contains("password") ||
               hint.contains("passwd") ||
               hint.contains("pass");
    }
}