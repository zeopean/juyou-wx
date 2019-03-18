package com.juyou.wx.util;

import com.juyou.wx.util.logger.LogUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created with idea
 * Description:
 *
 * @author zeopean
 * Date: 2018-06-28
 */
public class FileUtil {

    final static int BYTE_SIZE = 10240;


    /**
     * 通过url 获取 InputStream
     *
     * @param objUrl
     * @return
     */
    public static InputStream getInputStreamByUrl(String objUrl) {
        try {
            // 判断文件bytes 是否存在
            if (GeneralUtil.isNull(objUrl)) {
                return null;
            }

            URL url = new URL(objUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);

            //得到输入流
            return conn.getInputStream();

        } catch (Exception e) {

            LogUtil.error(e, 0, "Exception");

        }
        return null;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) {
        try {
            byte[] buffer = new byte[BYTE_SIZE];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            return bos.toByteArray();

        } catch (IOException ex) {
            LogUtil.error(ex, 0, "IOException");
        }
        return null;
    }

    /**
     * 新建文件
     *
     * @param bfile
     * @param fileName
     * @return
     */
    public static File bytesCreateFile(byte[] bfile, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File("/tmp/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }


    public static String generateFilename() {
        return UUID.randomUUID().toString();
    }

}
