package com.phenix.tools.image;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
import javax.imageio.ImageIO;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片压缩工具类
 */
public class ImageUtils {
    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 将base64图片转换成文件
     *
     * @param imageBase64
     * @param fileName
     * @return
     */
    public static Boolean decoderBase64ToImageFile(String imageBase64, String fileName) {
        if (imageBase64 == null) {
            return false;
        }
        Decoder decoder = Base64.getDecoder();
        try {
            // 解密
            byte[] b = decoder.decode(imageBase64);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(fileName);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将Url地址的图片转换成base64
     *
     * @param imageUrl
     * @return
     */
    public static String encoderBase64ForImageUrl(String imageUrl) {
        byte[] data = null;
        try {
            // 创建URL
            URL url = new URL(imageUrl);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            data = new byte[inStream.available()];
            inStream.read(data);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        Encoder encoder = Base64.getEncoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encodeToString(data);
    }

    /**
     * 将本地的图片转换成base64
     *
     * @param imagePath
     * @return
     */
    public static String encoderBase64ForImagePath(String imagePath) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imagePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        // 对字节数组Base64编码
        Encoder encoder = Base64.getEncoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encodeToString(data);
    }

    /**
     * 压缩图片文件成文件
     *
     * @param file       需要压缩的文件
     * @param targetFile 目标文件
     * @param width      需要压缩的宽度
     * @param height     需要压缩的高度
     * @return 是否成功
     */
    public static Boolean compressToFile(String file, String targetFile, Integer width, Integer height) {
        try {
            /*
             * size(width,height) 若图片横比200小，高比300小，不变
             * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
             * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
             */
            Thumbnails.of(file).size(width, height).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 压缩图片文件成文件
     *
     * @param inputStream 需要压缩的文件流
     * @param targetFile  目标文件
     * @param width       需要压缩的宽度
     * @param height      需要压缩的高度
     * @return 是否成功
     */
    public static Boolean compressToFile(InputStream inputStream, String targetFile, Integer width, Integer height) {
        try {
            /*
             * size(width,height) 若图片横比200小，高比300小，不变
             * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
             * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
             */
            Thumbnails.of(inputStream).size(width, height).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 压缩图片文件成文件
     *
     * @param file         需要压缩的文件
     * @param outputStream 目标文件流
     * @param width        需要压缩的宽度
     * @param height       需要压缩的高度
     * @return 是否成功
     */
    public static Boolean compressToStream(String file, OutputStream outputStream, Integer width, Integer height) {
        try {
            /*
             * size(width,height) 若图片横比200小，高比300小，不变
             * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
             * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
             */
            Thumbnails.of(file).size(width, height).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }


    /**
     * 压缩图片流成图片流
     *
     * @param inputStream  需要压缩的流
     * @param outputStream 目标文件流
     * @param width        需要压缩的宽度
     * @param height       需要压缩的高度
     * @return 是否成功
     */
    public static Boolean compressToStream(InputStream inputStream, OutputStream outputStream, Integer width, Integer height) {
        try {
            /*
             * size(width,height) 若图片横比200小，高比300小，不变
             * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
             * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
             */
            Thumbnails.of(inputStream).size(width, height).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 按比例进行缩放
     *
     * @param file       需要压缩的文件
     * @param targetFile 目标文件
     * @param scale      缩放比例
     * @return 是否成功
     */
    public static Boolean scaleToFile(String file, String targetFile, Double scale) {
        try {
            /**
             * scale(比例)
             */
            Thumbnails.of(file).scale(scale).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 按比例进行缩放
     *
     * @param inputStream 输入流
     * @param targetFile  目标文件
     * @param scale       缩放比例
     * @return 是否成功
     */
    public static Boolean scaleToFile(InputStream inputStream, String targetFile, Double scale) {
        try {
            /**
             * scale(比例)
             */
            Thumbnails.of(inputStream).scale(scale).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 按比例进行缩放
     *
     * @param file         文件
     * @param outputStream 输出流
     * @param scale        缩放比例
     * @return 是否成功
     */
    public static Boolean scaleToStream(String file, OutputStream outputStream, Double scale) {
        try {
            /**
             * scale(比例)
             */
            Thumbnails.of(file).scale(scale).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 按比例进行缩放
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param scale        缩放比例
     * @return 是否成功
     */
    public static Boolean scaleToStream(InputStream inputStream, OutputStream outputStream, Double scale) {
        try {
            /**
             * scale(比例)
             */
            Thumbnails.of(inputStream).scale(scale).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 不按照比例，指定大小进行缩放
     *
     * @param file       需要压缩的文件
     * @param targetFile 目标文件
     * @param width      需要缩放的宽度
     * @param height     需要缩放的高度
     * @return 是否成功
     */
    public static Boolean keepAspectRatioToFile(String file, String targetFile, Integer width, Integer height) {
        try {
            /**
             * keepAspectRatio(false) 默认是按照比例缩放的
             */
            Thumbnails.of(file).size(width, height).keepAspectRatio(false).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 不按照比例，指定大小进行缩放
     *
     * @param inputStream 输入流
     * @param targetFile  目标文件
     * @param width       需要缩放的宽度
     * @param height      需要缩放的高度
     * @return 是否成功
     */
    public static Boolean keepAspectRatioToFile(InputStream inputStream, String targetFile, Integer width, Integer height) {
        try {
            /**
             * keepAspectRatio(false) 默认是按照比例缩放的
             */
            Thumbnails.of(inputStream).size(width, height).keepAspectRatio(false).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 不按照比例，指定大小进行缩放
     *
     * @param file         文件
     * @param outputStream 输出流
     * @param width        需要缩放的宽度
     * @param height       需要缩放的高度
     * @return 是否成功
     */
    public static Boolean keepAspectRatioToStream(String file, OutputStream outputStream, Integer width, Integer height) {
        try {
            /**
             * keepAspectRatio(false) 默认是按照比例缩放的
             */
            Thumbnails.of(file).size(width, height).keepAspectRatio(false).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 不按照比例，指定大小进行缩放
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param width        需要缩放的宽度
     * @param height       需要缩放的高度
     * @return 是否成功
     */
    public static Boolean keepAspectRatioToStream(InputStream inputStream, OutputStream outputStream, Integer width, Integer height) {
        try {
            /**
             * keepAspectRatio(false) 默认是按照比例缩放的
             */
            Thumbnails.of(inputStream).size(width, height).keepAspectRatio(false).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 旋转
     *
     * @param file       需要旋转的文件
     * @param targetFile 目标文件
     * @param angle      旋转角度（正数：顺时针 负数：逆时针）
     * @return 是否成功
     */
    public static Boolean rotateToFile(String file, String targetFile, Double angle) {
        try {
            /**
             * rotate(角度),正数：顺时针 负数：逆时针
             */
            Thumbnails.of(file).rotate(angle).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 旋转
     *
     * @param inputStream 输入流
     * @param targetFile  目标文件
     * @param angle       旋转角度（正数：顺时针 负数：逆时针）
     * @return 是否成功
     */
    public static Boolean rotateToFile(InputStream inputStream, String targetFile, Double angle) {
        try {
            /**
             * rotate(角度),正数：顺时针 负数：逆时针
             */
            Thumbnails.of(inputStream).rotate(angle).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 旋转
     *
     * @param file         文件
     * @param outputStream 输出流
     * @param angle        旋转角度（正数：顺时针 负数：逆时针）
     * @return 是否成功
     */
    public static Boolean rotateToStream(String file, OutputStream outputStream, Double angle) {
        try {
            /**
             * rotate(角度),正数：顺时针 负数：逆时针
             */
            Thumbnails.of(file).rotate(angle).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 旋转
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param angle        旋转角度（正数：顺时针 负数：逆时针）
     * @return 是否成功
     */
    public static Boolean rotateToStream(InputStream inputStream, OutputStream outputStream, Double angle) {
        try {
            /**
             * rotate(角度),正数：顺时针 负数：逆时针
             */
            Thumbnails.of(inputStream).rotate(angle).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 水印
     *
     * @param file          需要压缩的文件
     * @param targetFile    目标文件
     * @param waterMarkFile 水印文件
     * @param positions     水印位置
     * @return 是否成功
     */
    public static Boolean waterMarkToFile(String file, String targetFile, String waterMarkFile, Positions positions) {
        try {
            /**
             * watermark(位置，水印图，透明度)
             */
            Thumbnails.of(file).watermark(positions, ImageIO.read(new File(waterMarkFile)), 0.5f)
                    .toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 水印
     *
     * @param inputStream   输入流
     * @param targetFile    目标文件
     * @param waterMarkFile 水印文件
     * @param positions     水印位置
     * @return 是否成功
     */
    public static Boolean watermarkToFile(InputStream inputStream, String targetFile, String waterMarkFile, Positions positions) {
        try {
            /**
             * watermark(位置，水印图，透明度)
             */
            Thumbnails.of(inputStream).watermark(positions, ImageIO.read(new File(waterMarkFile)), 0.5f)
                    .toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 水印
     *
     * @param file          文件
     * @param outputStream  输出流
     * @param waterMarkFile 水印文件
     * @param positions     水印位置
     * @return 是否成功
     */
    public static Boolean watermarkToStream(String file, OutputStream outputStream, String waterMarkFile, Positions positions) {
        try {
            /**
             * watermark(位置，水印图，透明度)
             */
            Thumbnails.of(file).watermark(positions, ImageIO.read(new File(waterMarkFile)), 0.5f)
                    .toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 水印
     *
     * @param inputStream   输入流
     * @param outputStream  输出流
     * @param waterMarkFile 水印文件
     * @param positions     水印位置
     * @return 是否成功
     */
    public static Boolean watermarkToStream(InputStream inputStream, OutputStream outputStream, String waterMarkFile, Positions positions) {
        try {
            /**
             * watermark(位置，水印图，透明度)
             */
            Thumbnails.of(inputStream).watermark(positions, ImageIO.read(new File(waterMarkFile)), 0.5f)
                    .toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 裁剪
     *
     * @param file               需要压缩的文件
     * @param targetFile         目标文件
     * @param positions          裁剪位置
     * @param sourceRegionWidth  裁剪宽度
     * @param sourceRegionHeight 裁剪高度
     * @return 是否成功
     */
    public static Boolean sourceRegionToFile(String file, String targetFile, Positions positions, Integer sourceRegionWidth, Integer sourceRegionHeight) {
        try {
            /**
             * 裁剪
             */
            Thumbnails.of(file).sourceRegion(positions, sourceRegionWidth, sourceRegionHeight).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 裁剪
     *
     * @param inputStream        输入流
     * @param targetFile         目标文件
     * @param positions          裁剪位置
     * @param sourceRegionWidth  裁剪宽度
     * @param sourceRegionHeight 裁剪高度
     * @return 是否成功
     */
    public static Boolean sourceRegionToFile(InputStream inputStream, String targetFile, Positions positions, Integer sourceRegionWidth, Integer sourceRegionHeight) {
        try {
            /**
             * 裁剪
             */
            Thumbnails.of(inputStream).sourceRegion(positions, sourceRegionWidth, sourceRegionHeight).toFile(targetFile);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 裁剪
     *
     * @param file               文件
     * @param outputStream       输出流
     * @param positions          裁剪位置
     * @param sourceRegionWidth  裁剪宽度
     * @param sourceRegionHeight 裁剪高度
     * @return 是否成功
     */
    public static Boolean sourceRegionToStream(String file, OutputStream outputStream, Positions positions, Integer sourceRegionWidth, Integer sourceRegionHeight) {
        try {
            /**
             * 裁剪
             */
            Thumbnails.of(file).sourceRegion(positions, sourceRegionWidth, sourceRegionHeight).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    /**
     * 裁剪
     *
     * @param inputStream        输入流
     * @param outputStream       输出流
     * @param positions          裁剪位置
     * @param sourceRegionWidth  裁剪宽度
     * @param sourceRegionHeight 裁剪高度
     * @return 是否成功
     */
    public static Boolean sourceRegionToStream(InputStream inputStream, OutputStream outputStream, Positions positions, Integer sourceRegionWidth, Integer sourceRegionHeight) {
        try {
            /**
             * 裁剪
             */
            Thumbnails.of(inputStream).sourceRegion(positions, sourceRegionWidth, sourceRegionHeight).toOutputStream(outputStream);
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

}
