package com.dpforge.serialmidi.util;

public class TextUtils {
    private TextUtils() {

    }

    public static boolean isNullOrEmpty(final CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }
}
