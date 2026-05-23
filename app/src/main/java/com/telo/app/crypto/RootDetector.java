package com.telo.app.crypto;

import android.content.Context;
import android.content.pm.PackageManager;
import java.io.File;

public class RootDetector {

    private static final String[] ROOT_BINARIES = {
        "/system/bin/su",
        "/system/xbin/su",
        "/sbin/su",
        "/system/su",
        "/system/bin/.ext/.su",
        "/system/usr/we-need-root/su-backup",
        "/system/xbin/mu"
    };

    private static final String[] ROOT_PACKAGES = {
        "com.noshufou.android.su",
        "com.noshufou.android.su.elite",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.thirdparty.superuser",
        "com.topjohnwu.magisk",
        "io.github.vvb2060.magisk",
        "com.kingroot.kinguser",
        "com.kingo.root"
    };

    // ── Check ─────────────────────────────────────────────────

    public static boolean isRooted(Context context) {
        return checkRootBinaries() ||
               checkRootPackages(context) ||
               checkBuildTags();
    }

    private static boolean checkRootBinaries() {
        for (String path : ROOT_BINARIES) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootPackages(Context context) {
        PackageManager pm = context.getPackageManager();
        for (String pkg : ROOT_PACKAGES) {
            try {
                pm.getPackageInfo(pkg, 0);
                return true;
            } catch (PackageManager.NameNotFoundException ignored) {}
        }
        return false;
    }

    private static boolean checkBuildTags() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }
}