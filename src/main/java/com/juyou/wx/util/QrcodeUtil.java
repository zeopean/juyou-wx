package com.juyou.wx.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码生成和解析工具
 *
 * @author zeopean
 */
public class QrcodeUtil {

    /**
     * 前景色
     */
    private final static int ON_COLOR = 0xFF000000;
    /**
     * 背景色
     */
    private final static int OFF_COLOR = 0xFFFFFFFF;
    /**
     * 二维码默认宽度
     */
    private final static int DEFAULT_WIDTH = 205;
    /**
     * 默认图片格式
     */
    private final static String IMG_FORMAT_NAME = "jpg";

    private static Map<EncodeHintType, Object> encodeHints = new Hashtable<EncodeHintType, Object>();
    private static Map<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();

    static {
        encodeHints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        encodeHints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        encodeHints.put(EncodeHintType.MARGIN, 1);

        decodeHints.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    static {
        // 避免linux无输出设备提示X11服务异常
        System.setProperty("java.awt.headless", "true");
    }

    private static BitMatrix createMatrix(String text, int width) throws WriterException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, width, encodeHints);
        return bitMatrix;
    }

    /**
     * 生成二维码
     *
     * @param text  内容
     * @param width 二维码图片width
     * @return
     * @throws WriterException
     */
    public static BufferedImage createQRCodeImage(String text, int width) throws WriterException {
        BitMatrix output = createMatrix(text, width);
        MatrixToImageConfig config = new MatrixToImageConfig(ON_COLOR, OFF_COLOR);
        return MatrixToImageWriter.toBufferedImage(output, config);
    }

    /**
     * 生成二维码，默认图片大小是205x205
     *
     * @param text
     * @return
     * @throws WriterException
     */
    public static BufferedImage createQRCodeImage(String text) throws WriterException {
        return createQRCodeImage(text, DEFAULT_WIDTH);
    }

    /**
     * 二维码图片写入文件
     *
     * @param url
     * @throws IOException
     */
    public static void writeToFile(String url, String fileName) throws IOException {
        try {
            int width = 170;
            BufferedImage image = createQRCodeImage(url, width);
            File file = new File(fileName);
            if (!ImageIO.write(image, IMG_FORMAT_NAME, file)) {
                throw new IOException("Could not write an image of format png to " + file);
            } else {
                System.out.println("图片生成成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 解析二维码
     * 如果图片不存在，返回null
     *
     * @return 编码在二维码图片上的信息
     * @throws NotFoundException
     */
    public static final String decodeQrcode(BufferedImage bufferedImage) throws NotFoundException {
        if (bufferedImage == null) {
            return null;
        }
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        //基于二维码图片文件解码获得其结果
        Result result = new MultiFormatReader().decode(bitmap, decodeHints);
        return result.getText();
    }
}
