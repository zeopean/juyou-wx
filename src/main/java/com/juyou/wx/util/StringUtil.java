package com.juyou.wx.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-15
 */
public class StringUtil {

    final static String isEnglishPattern = "[a-zA-Z]+";

    public static boolean isEnglish(String str) {
        Pattern pattern = Pattern.compile(isEnglishPattern);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return
     */
    public static String readToString(String filePath) {
        File file = new File(filePath);
        // 获取文件长度
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(filecontent);
    }


    private static Random randGen = null;

    private static char[] numbersAndLetters = null;

    /**
     * 生成随机字符串
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
            numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }

        return new String(randBuffer);
    }

    /**
     * long 数组转成字符串
     *
     * @param lists
     * @param mark
     * @return
     */
    public static String longListToString(List<Long> lists, String mark) {
        if (lists == null) {
            return "";
        }
        Collections.sort(lists);
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        Collections.sort(lists);
        for (long item : lists) {
            if (flag) {
                result.append(mark);
            } else {
                flag = true;
            }
            result.append(item);
        }
        return result.toString();
    }

    /**
     * 过滤掉超过3个字节的UTF8字符
     *
     * @param text
     * @return
     */
    public static String filterOffUtf8Mb4(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        byte[] bytes = new byte[0];
        try {
            String charsetName = "utf-8";
            bytes = text.getBytes(charsetName);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            int i = 0;
            while (i < bytes.length) {
                short b = bytes[i];
                if (b > 0) {
                    buffer.put(bytes[i++]);
                    continue;
                }
                // 去掉符号位
                b += 256;
                if (((b >> 5) ^ 0x6) == 0) {
                    buffer.put(bytes, i, 2);
                    i += 2;
                } else if (((b >> 4) ^ 0xE) == 0) {
                    buffer.put(bytes, i, 3);
                    i += 3;
                } else if (((b >> 3) ^ 0x1E) == 0) {
                    i += 4;
                } else if (((b >> 2) ^ 0x3E) == 0) {
                    i += 5;
                } else if (((b >> 1) ^ 0x7E) == 0) {
                    i += 6;
                } else {
                    buffer.put(bytes[i++]);
                }
            }
            buffer.flip();
            return new String(buffer.array(), charsetName);

        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
