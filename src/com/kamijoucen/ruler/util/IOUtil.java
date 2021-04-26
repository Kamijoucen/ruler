package com.kamijoucen.ruler.util;

public class IOUtil {

    public final static boolean[] firstIdentifierFlags = new boolean[256];
    public final static boolean[] identifierFlags = new boolean[256];

    static {

        for (char c = 0; c < firstIdentifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                firstIdentifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                firstIdentifierFlags[c] = true;
            } else if (c == '_' || c == ':') {
                firstIdentifierFlags[c] = true;
            }
        }

        for (char c = 0; c < identifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                identifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                identifierFlags[c] = true;
            } else if (c == '_' || c == '-') {
                identifierFlags[c] = true;
            } else if (c >= '0' && c <= '9') {
                identifierFlags[c] = true;
            }
        }
    }

    public static boolean isFirstIdentifierChar(char ch) {
        return ch >= IOUtil.firstIdentifierFlags.length || IOUtil.firstIdentifierFlags[ch];
    }

    public static boolean isIdentifierChar(char ch) {
        return ch >= IOUtil.identifierFlags.length || IOUtil.identifierFlags[ch];
    }
}
