package net.yao.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.io.FileUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class FileUtilYao {

    /**
     * 生成文件名 确保文件名不可能重复
     * 用当前时间和随机的唯一字符串和原始文件名组合
     * param  文件名
     * return
     */
    public static String getFileName(String filename){
        return System.currentTimeMillis() + "_" + UUID.fastUUID().toString() + "_" + filename;
    }

    /**
     * 通过url读取远程文本内容
     */
    public static String readRemoteFile(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Remote file read error");
        }
    }

    /**
     * 新建目录
     * @param
     * @return
     */

    public static void mkdir(String dir) {
        FileUtil.mkdir(dir);
    }

    /**
     * 提取文件后缀名（扩展名）
     * @param remoteFilePath
     * @return
     */
    public static String getSuffix(String remoteFilePath) {
        if (remoteFilePath.contains(".")) {
            return remoteFilePath.substring(remoteFilePath.lastIndexOf("."));
        }
        return "";
    }



}
