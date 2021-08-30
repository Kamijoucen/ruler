package com.kamijoucen.ruler.util;

import java.io.*;

public class IOUtil {

    public final static boolean[] firstIdentifierFlags = new boolean[256];
    public final static boolean[] identifierFlags = new boolean[256];
    public final static boolean[] numberFlags = new boolean[256];

    static {

        for (char c = 0; c < numberFlags.length; ++c) {
            if (c >= '0' && c <= '9') {
                numberFlags[c] = true;
            }
        }

        for (char c = 0; c < firstIdentifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                firstIdentifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                firstIdentifierFlags[c] = true;
            } else if (c == '_') {
                firstIdentifierFlags[c] = true;
            }
        }

        for (char c = 0; c < identifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                identifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                identifierFlags[c] = true;
            } else if (c == '_') {
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

    public static String read(File file) {
        StringBuilder sb = new StringBuilder();
        Reader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            char[] buf = new char[2048];

            int len = 0;
            while ((len = reader.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String read(String path) {
        return read(new File(path));
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        return "".equals(str.trim());
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

}
