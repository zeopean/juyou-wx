package com.juyou.wx.service.card.helper;

import com.juyou.wx.common.constants.ResponseData;
import com.juyou.wx.service.card.bean.Picture;
import com.juyou.wx.util.StringUtil;
import com.juyou.wx.util.logger.LogUtil;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 图片处理工具
 *
 * @author zeopean
 */
public final class ImageHelper {

    private static Map<String, byte[]> picBytesMap = new HashMap<>();


    private byte[] getImageBytes(String filePath) {
        byte[] bytes = picBytesMap.get(filePath);
        if (bytes != null) {
            return bytes;

        }
        bytes = file2byte(filePath);
        if (bytes != null) {
            picBytesMap.put(filePath, bytes);

        }
        return bytes;
    }

    private byte[] file2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return buffer;
    }

    private static InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }


    /**
     * @param srcFile 源图片、targetFile截好后图片全名、startAcross 开始截取位置横坐标、StartEndlong开始截图位置纵坐标、width截取的长，hight截取的高
     * @Description:截图
     */
    public static void cutImage(String srcFile, String targetFile, int startAcross, int StartEndlong, int width, int hight) throws Exception {
        // 取得图片读入器
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = readers.next();
        // 取得图片读入流
        InputStream source = new FileInputStream(srcFile);
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        // 图片参数对象
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(startAcross, StartEndlong, width, hight);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, targetFile.split("\\.")[1], new File(targetFile));
    }

    /**
     * @param files 要拼接的文件列表
     * @param type  横向拼接， 2 纵向拼接
     */
    public static void mergeImage(String[] files, int type, String targetFile) {
        int len = files.length;
        if (len < 1) {
            throw new RuntimeException("图片数量小于1");
        }
        File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                src[i] = new File(files[i]);
                images[i] = ImageIO.read(src[i]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            // 横向
            if (type == 1) {
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) {
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                newHeight += images[i].getHeight();
            }
        }
        if (type == 1 && newWidth < 1) {
            return;
        }
        if (type == 2 && newHeight < 1) {
            return;
        }

        // 生成新图片
        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
                            images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            //输出想要的图片
            ImageIO.write(ImageNew, targetFile.split("\\.")[1], new File(targetFile));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 小图片贴到大图片形成一张图(合成)
     */
    public static BufferedImage overlapImage(BufferedImage background, BufferedImage small, int x, int y) {
        try {
            if (null == background) {
                return null;
            }
            if (null == small) {
                return background;
            }
            Graphics2D g = background.createGraphics();
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
            // 使用 setRenderingHint 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.dispose();
            return background;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param img
     * @param text
     * @param color
     * @param font
     * @param x
     * @param y
     * @return
     */
    public static BufferedImage textCompositionImage(BufferedImage img, String text, Color color, Font font, int x, int y) {

        try {
            int w = img.getWidth();
            int h = img.getHeight();
            Graphics2D g = img.createGraphics();
            //设置字体颜色
            g.setColor(color);
            g.setFont(font);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            g.drawString(text, x, y);
            g.dispose();
        } catch (Exception e) {
        }

        return img;
    }

    /**
     * 进行长文本图片合成
     *
     * @param image
     * @param picture
     * @return
     */
    public static BufferedImage textMuilLineImage(BufferedImage image, Picture picture) {

        try {
            Graphics2D g = image.createGraphics();
            Color color = new Color(picture.getPicFont().getPicColor().getR(), picture.getPicFont().getPicColor().getG(), picture.getPicFont().getPicColor().getB());
            Font font;
            if (1 == picture.getPicFont().getIsBold()) {
                font = FontHelper.getBoldFont(picture.getPicFont().getSize());
            } else {
                font = FontHelper.getMediumFont(picture.getPicFont().getSize());
            }
            // 设置字体颜色
            g.setColor(color);
            g.setFont(font);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            FontMetrics fm = g.getFontMetrics(font);
            // 获取字体高度
            int fontHeight = fm.getHeight() + picture.getPicFont().getLineHeight();

            char[] contentArr = picture.getPicFont().getContent().toCharArray();
            // 字符的宽度
            int maxCharWidth = fm.charWidth("中文".charAt(0));
            // 英文宽度
            int enCharWidth = fm.charWidth("a".charAt(0));

            // 计算字符长度
            int charWidth = 0;
            int lineNums = 0;
            int index = 0;
            String tmp = "";
            int x = picture.getStartX(), y = picture.getStartY();

            for (char con : contentArr) {
                if (charWidth <= picture.getWidth() - maxCharWidth) {
                    char nextChar = ' ';
                    if (index < contentArr.length - 1) {
                        nextChar = contentArr[index + 1];
                    }
                    // 处理英文
                    if (charWidth > picture.getWidth() - 2 * maxCharWidth && StringUtil.isEnglish(String.valueOf(con))) {
                        tmp = tmp + String.valueOf(con);

                        if (String.valueOf(nextChar).equals(" ")) {
                            lineNums++;
                            index++;
                            if (lineNums > 1) {
                                // 绘制一行文字
                                g.drawString(tmp, x, (y + (lineNums - 1) * fontHeight));

                            } else {
                                // 绘制一行文字
                                g.drawString(tmp, x, y);
                            }
                            charWidth = 0;
                            tmp = "";
                            continue;
                        }
                    }

                    // 处理双换行
                    if (String.valueOf(con).equals("\n") && String.valueOf(nextChar).equals("\n")) {
                        lineNums++;
                        if (lineNums > 1) {
                            // 绘制一行文字
                            g.drawString(tmp, x, (y + (lineNums - 1) * fontHeight));

                        } else {
                            // 绘制一行文字
                            g.drawString(tmp, x, y);
                        }
                        charWidth = 0;
                        tmp = "";
                        index++;
                        continue;
                    }

                    // 换行符的设置
                    if ((String.valueOf(con).equals("\r") && String.valueOf(nextChar).equals("\n")) ||
                            (String.valueOf(con).equals("\n"))) {
                        if (null != tmp) {
                            index++;
                            lineNums++;
                            // 绘制一行文字
                            g.drawString(tmp, x, (y + (lineNums - 1) * fontHeight));
                            charWidth = 0;
                            tmp = "";
                            continue;
                        }
                    }
                    charWidth += fm.charWidth(con);
                    tmp = tmp + String.valueOf(con);

                    // 最后一行, 后面无字符
                    if (index == contentArr.length - 1) {
                        if (lineNums > 0) {
                            lineNums++;
                        }
                        if (lineNums > 1) {
                            // 绘制一行文字
                            g.drawString(tmp, x, (y + (lineNums - 1) * fontHeight));

                        } else {
                            // 绘制一行文字
                            g.drawString(tmp, x, y);
                        }
                        break;
                    }
                    index++;

                } else {
                    if (lineNums > 0 || index != contentArr.length - 1) {
                        lineNums++;
                    }
                    tmp = tmp + String.valueOf(con);
                    if (lineNums > 1) {
                        // 绘制一行文字
                        g.drawString(tmp, x, (y + (lineNums - 1) * fontHeight));

                    } else {
                        // 绘制一行文字
                        g.drawString(tmp, x, y);
                    }
                    charWidth = 0;
                    tmp = "";
                    index++;

                }
            }

            g.dispose();

        } catch (Exception e) {
            LogUtil.error(e, ResponseData.REQUEST_FAIL);
        }

        return image;
    }


    /**
     * 把方形图片转成圆形
     *
     * @param picture
     * @return
     * @throws IOException
     */
    public static BufferedImage convertCircular(Picture picture, BufferedImage image) {

        try {
            if (null == image) {
                return null;
            }
            if (0 == picture.getIsCircle()) {
                return image;
            }
            //透明底的图片
            BufferedImage backgroundImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, image.getWidth(), image.getHeight());
            Graphics2D g2 = backgroundImage.createGraphics();
            g2.setClip(shape);
            g2.drawImage(image, 0, 0, null);
            // 使用 setRenderingHint 设置抗锯齿
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //设置颜色
            g2.setBackground(Color.green);
            g2.dispose();
            return zoomImage(backgroundImage, picture.getWidth(), picture.getHeight());

        } catch (Exception e) {
            LogUtil.error(e, 0, "Exception");

        }
        return null;
    }

    /**
     * 图片缩放功能
     *
     * @param image
     * @param width
     * @param height
     * @throws Exception
     */
    public static BufferedImage zoomImage(BufferedImage image, int width, int height) throws Exception {

        double wr = 0, hr = 0;
        //设置缩放目标图片模板
        Image Itemp = image.getScaledInstance(width, height, image.SCALE_SMOOTH);

        // 获取缩放比例
        wr = width * 1.0 / image.getWidth();
        hr = height * 1.0 / image.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(image, null);
        try {
            image = (BufferedImage) Itemp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return image;
    }


    /**
     * 进行文本行图片合成
     */
    public static BufferedImage textLineImage(BufferedImage image, String content, Color color, Color red, int fontSize, int lineHeight, int posX, int posY, boolean isNumberBold, boolean isBold) {
        try {
            Font font = FontHelper.getMediumFont(fontSize);
            if (isBold) {
                font = FontHelper.getBoldFont(fontSize);
            }
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            FontMetrics fm = g.getFontMetrics(font);
            // 获取字体高度
            int fontHeight = fm.getHeight() + lineHeight;

            char[] contentArr = content.toCharArray();

            // 计算字符长度
            int index = 0;
            for (char con : contentArr) {
                if (StringUtils.isNumeric(String.valueOf(con))) {
                    if (isNumberBold) {
                        // 字体设置为粗体
                        g.setFont(FontHelper.getBoldFont(fontSize));
                    }
                    g.setColor(red);
                } else {
                    g.setFont(font);
                    // 设置字体颜色
                    g.setColor(color);
                }

                // 绘制一行文字
                g.drawString(String.valueOf(con), posX + index, posY + fontHeight);
                // 字符的宽度
                index += fm.charWidth(con);
            }

            g.dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * 获取文本的宽度
     *
     * @param image
     * @param content
     * @param font
     * @return
     */
    public static int getFontsWidth(BufferedImage image, String content, Font font) {
        Graphics2D g = image.createGraphics();
        FontMetrics fm = g.getFontMetrics(font);

        char[] contentArr = content.toCharArray();
        int charWidth = 0;
        for (char c : contentArr) {
            charWidth += fm.charWidth(c);
        }
        return charWidth;
    }


    // 绘制直线
    public static BufferedImage createLineImage(int x, int y, float stroke) {
        BufferedImage image = new BufferedImage(y, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.drawLine(0, x, y, x);
        g.setStroke(new BasicStroke(stroke));
        g.dispose();
        return image;
    }

    /**
     * 图片居中裁剪
     *
     * @param src
     * @param w
     * @param h
     * @return
     * @throws IOException
     */
    public static BufferedImage cut(String src, int posX, int posY, int w, int h) throws IOException {
        String[] exts = src.split("\\.");
        Iterator iterator = ImageIO.getImageReadersByFormatName(exts[exts.length - 1]);
        ImageReader reader = (ImageReader) iterator.next();

        //通过输入流获取图片数据
        InputStream inStream = new FileInputStream(src);

        ImageInputStream iiStream = ImageIO.createImageInputStream(inStream);
        reader.setInput(iiStream, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(posX, posY, w, h);
        param.setSourceRegion(rect);
        return reader.read(0, param);

    }

    /**
     * 图片缩放功能
     *
     * @param image
     * @param width
     * @param height
     * @throws Exception
     */
    public static BufferedImage zoomAndCutImage(BufferedImage image, int width, int height) throws Exception {

        double wr = 0;
        //设置缩放目标图片模板
        Image Itemp = image.getScaledInstance(width, height, image.SCALE_SMOOTH);

        //获取缩放比例
        wr = width * 1.0 / image.getWidth();

        // 等比例缩放
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, wr), null);
        Itemp = ato.filter(image, null);
        try {
            image = (BufferedImage) Itemp;
            // 图片先写入临时文件
            String tmpDir = "/tmp/zoom-pic/";
            File dir = new File(tmpDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = tmpDir + UUID.randomUUID().toString() + ".jpg";
            File file = new File(fileName);
            // 获取处理后的图片
            if (!ImageIO.write(image, "jpg", file)) {
                throw new IOException("Could not write an image of format png to " + file);
            }

            int startY = (int) Math.ceil((double) (image.getHeight() - height) / (double) 2);

            // 进行图片裁剪
            image = cut(fileName, 0, startY, width, height);
            // 删除该临时文件
            file.delete();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return image;
    }


}