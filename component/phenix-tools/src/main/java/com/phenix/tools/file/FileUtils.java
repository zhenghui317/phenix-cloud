package com.phenix.tools.file;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.FilerException;
import java.io.*;
import java.util.UUID;

/**
 * 文件操作类
 *
 * @author zhenghui
 * @date 2018-12-18
 */
public class FileUtils {
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 获取临时目录地址
     *
     * @return
     */
    public static String getTempDir() {
        String tempDir = System.getProperty("user.dir") + "/data/tmp/";
        File tmpFile = new File(tempDir);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        return tempDir;
    }

    /**
     * 获取临时文件地址
     *
     * @param extension
     * @return
     */
    public static String getTempFilePath(String extension) {
        return getTempDir() + "/" + UUID.randomUUID().toString().replace("-", "") + "." + extension;
    }

    /**
     * 获取临时文件地址
     *
     * @return
     */
    public static String getTempFilePath() {
        return getTempDir() + "/" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if (StringUtils.isBlank(filename)) {
            return "";
        }
        int dot = filename.lastIndexOf('.');
        if ((dot > -1) && (dot < (filename.length() - 1))) {
            return filename.substring(dot + 1);
        }
        return "";
    }

    /**
     * 获取文件名
     *
     * @param filename
     * @return
     */
    public static String getFileName(String filename) {
        if (StringUtils.isBlank(filename)) {
            return "";
        }
        int dot = filename.lastIndexOf('/');
        if ((dot > -1) && (dot < (filename.length() - 1))) {
            return filename.substring(dot + 1);
        }
        return "";
    }

    /**
     * 删除文件
     *
     * @param filePath 要删除的文件
     * @return
     */
    public static Boolean delFile(String filePath) {
        try {
            if (StringUtils.isBlank(filePath)) {
                return false;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
            if (!file.isFile()) {
                throw new FilerException("当前删除的不是文件");
            }
            file.delete();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*** 强制删除文件或文件夹下的文件 ***/
    public static void deleteAllFilesOfDir(File path) {
        if (null != path) {
            if (!path.exists()) {
                return;
            }
            if (path.isFile()) {
                boolean result = path.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    // 回收资源
                    System.gc();
                    result = path.delete();
                }
                return;
            }
            File[] files = path.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    deleteAllFilesOfDir(files[i]);
                }
            }
            path.delete();
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径
     */
    public static Boolean delFolder(String folderPath) {
        try {
            if (StringUtils.isBlank(folderPath)) {
                return false;
            }
            //删除完里面所有内容
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            //删除空文件夹
            myFilePath.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     */
    public static Boolean delAllFile(String path) {
        boolean flag = false;
        if (StringUtils.isBlank(path)) {
            return flag;
        }
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                //先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                //再删除空文件夹
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    public static String writeFile(byte[] bfile, String fileName) {
        String dirPath = FileUtils.getTempDir();
        String filePath = dirPath + "/" + fileName;

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(dirPath);
            //判断文件目录是否存在
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
    }

    /**
     * 获取文件md5
     *
     * @param filePath
     * @return
     */
    public static String getMD5(String filePath) {
        String md5 = "";
        if (StringUtils.isBlank(filePath)) {
            return md5;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return md5;
        }
        try {
            md5 = DigestUtils.md5Hex(new FileInputStream(filePath));
            return md5;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return md5;
        }
    }
}
