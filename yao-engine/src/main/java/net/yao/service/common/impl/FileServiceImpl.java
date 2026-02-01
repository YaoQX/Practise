package net.yao.service.common.impl;

import cn.hutool.core.util.IdUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.yao.config.MinIoConfig;
import net.yao.service.common.FileService;
import net.yao.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private MinIoConfig minIoConfig;

    @Resource
    private MinioClient minioClient;

    /**
     * 获取文件名称
     * 生成随机名称
     * 文件上传
     * 拼接路径返回
     *
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {

        String filename = FileUtil.getFileName(file.getOriginalFilename());

        upload(file, filename);

        String url = minIoConfig.getEndpoint() + "/" + minIoConfig.getBucketName() + "/" + filename;

        return url;
    }

    @Override
    public String getTempAccessFileUrl(String remoteFilePath) {
        try {
            String filename = remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
            GetPresignedObjectUrlArgs objectUrlArgs = GetPresignedObjectUrlArgs.builder().bucket(minIoConfig.getBucketName())
                    .object(filename)
                    .expiry(60, TimeUnit.MINUTES)
                    .method(Method.GET)
                    .build();
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(objectUrlArgs);

            return presignedObjectUrl;
        }catch (Exception e){
            log.error("Failed to obtain temporary file access link",e);
            throw new RuntimeException("Failed to obtain temporary file access link");
        }
    }

    /**
     * 以url读取远程文件
     * @param remoteFilePath
     * @return
     */
    public String copyRemoteFileToLocalTempFile(String remoteFilePath) {
        // 拼接本地临时文件路径
        String localTempFilePath = System.getProperty("user.dir")+ File.separator+"static"+File.separator
                + IdUtil.simpleUUID() + FileUtil.getSuffix(remoteFilePath);
        // 创建临时文件夹
        FileUtil.mkdir(localTempFilePath);
        try {
            // 获取临时访问文件的URL地址
            String tempAccessFileUrl = getTempAccessFileUrl(remoteFilePath);
            URL url =  new URL(tempAccessFileUrl);
            // 打开远程文件的输入流
            InputStream inputStream = url.openStream();

            // 创建本地文件对象
            Path localFile = Path.of(localTempFilePath);

            // 将远程文件复制到本地文件
            Files.copy(inputStream, localFile, StandardCopyOption.REPLACE_EXISTING);

            // 关闭输入流
            inputStream.close();
            // 返回本地文件路径
            return localFile.toFile().getPath();
        } catch (Exception e){
            // 记录错误日志
            log.error(e.getMessage());
            // 抛出异常：读取远程文件失败
            throw new RuntimeException("读取远程文件失败");
        }
    }


    /**
     * 文件上传
     *
     * @param file
     * @param filename
     */
    private void upload(MultipartFile file, String filename) {
        if (file == null || file.getSize() == 0) {
            throw new RuntimeException("File is none");
        } else {
            try {
                InputStream inputStream = file.getInputStream();
                PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                        .bucket(minIoConfig.getBucketName()) // 寄到哪里去？（桶名）
                        .object(filename)                    // 到了那边叫什么名字？（文件名）
                        .stream(inputStream, file.getSize(), -1) // 核心：货物是什么？分片大小默认
                        .contentType(file.getContentType())  // 货物类型（是图片还是文本？）
                        .build();
                minioClient.putObject(putObjectArgs);//发送
            } catch (Exception e) {
                throw new RuntimeException("File upload error");
            }
        }
    }
}
