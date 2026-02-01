package net.yao.controller.common;

import net.yao.service.common.FileService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 文件上传接口
     * @param file 前端传来的文件对象
     * @return
     */
    @PostMapping("/upload")
    public JsonData upload(@RequestParam("File") MultipartFile file){

        String path = fileService.upload(file);

        return JsonData.buildSuccess(path);
    }
}
