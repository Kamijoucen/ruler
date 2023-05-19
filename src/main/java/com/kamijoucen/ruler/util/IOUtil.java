package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.common.Constant;

import java.io.*;
import java.util.UUID;

public class IOUtil {

    public final static boolean[] pathFlags = new boolean[256];
    public final static boolean[] firstIdentifierFlags = new boolean[256];
    public final static boolean[] identifierFlags = new boolean[256];
    public final static boolean[] numberFlags = new boolean[256];

    static {

        for (char c = 0; c < pathFlags.length; ++c) {
            if (c == '.' || c == '/' || c == '\\' || c == ':' || c == '*' || c == '?' || c == '<'
                    || c == '>' || c == '|') {
                pathFlags[c] = false;
            } else {
                pathFlags[c] = true;
            }
        }

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

    public static boolean isAvailablePathChar(char ch) {
        return ch >= IOUtil.pathFlags.length || IOUtil.pathFlags[ch];
    }

    public static boolean isFirstIdentifierChar(char ch) {
        return ch >= IOUtil.firstIdentifierFlags.length || IOUtil.firstIdentifierFlags[ch];
    }

    public static boolean isIdentifierChar(char ch) {
        return ch >= IOUtil.identifierFlags.length || IOUtil.identifierFlags[ch];
    }

    public static String read(InputStream inputStream) {
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        return read(reader);
    }

    public static String read(File file) {
        try {
            Reader reader = new BufferedReader(new FileReader(file));
            return read(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String read(String path) {
        return read(new File(path));
    }

    public static String read(Reader reader) {
        StringBuilder sb = new StringBuilder();
        try {
            char[] buf = new char[2048];
            int len = 0;
            while ((len = reader.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }
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

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        return "".equals(str.trim());
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String getVirtualPath(String script, String alias) {
        return "virtual_path_" + UUID.randomUUID().toString() + "_" + alias.hashCode() + "_" + script.hashCode();
    }

    public static boolean isWhitespace(char ch) {
        return Character.isWhitespace(ch);
    }

//    public static boolean isWhitespace(char ch) {
//        return ch != Constant.ENTER && Character.isWhitespace(ch);
//    }

}
