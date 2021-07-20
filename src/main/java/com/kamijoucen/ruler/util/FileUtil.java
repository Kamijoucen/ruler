package com.kamijoucen.ruler.util;

import java.io.*;

public class FileUtil {

    public static String read(String path) {
        StringBuilder sb = new StringBuilder();
        Reader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));

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

}
