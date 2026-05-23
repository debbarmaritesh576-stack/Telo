package com.telo.app.util;

import android.graphics.Color;

public class ColorHelper {

    public static int parseColor(String hex) {
        try {
            return Color.parseColor(hex);
        } catch (Exception e) {
            return Color.GRAY;
        }
    }

    public static String toHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static int darken(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }

    public static int lighten(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = Math.min(1f, hsv[2] * factor);
        return Color.HSVToColor(hsv);
    }

    public static boolean isDark(int color) {
        double luminance = 0.299 * Color.red(color)
                         + 0.587 * Color.green(color)
                         + 0.114 * Color.blue(color);
        return luminance < 128;
    }

    public static int getContrastColor(int backgroundColor) {
        return isDark(backgroundColor) ? Color.WHITE : Color.BLACK;
    }
}