package com.mine.util;

import java.io.*;

public class FileUtils {
    /**
     * DOC 从文件里读取数据.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String readFromFile(String path) throws IOException {
        File file = new File(path);
        FileReader reader = new FileReader(file);
        char[] bb = new char[1024];
        String str = "";
        int n;
        while ((n = reader.read(bb)) != -1) {
            str += new String(bb, 0, n);
        }
        reader.close();
        return str;
    }

    /**
     * DOC 往文件里写入数据.
     *
     * @throws IOException
     */
    public static void writeToFile(String path,String content) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        writeToFile("/Users/gaoyang/key.txt","testtest2222");
        System.out.println(readFromFile("/Users/gaoyang/key.txt"));
    }
}
