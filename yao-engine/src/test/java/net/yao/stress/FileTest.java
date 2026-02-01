package net.yao.stress;

import jakarta.annotation.Resource;
import net.yao.EngineApplication;
import net.yao.service.common.FileService;
import net.yao.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = EngineApplication.class)
public class FileTest {

    @Resource
    private FileService fileService;

    @Test
    public void testTempFileApi(){
        String tempAccessFileUrl = fileService.getTempAccessFileUrl("http://18.181.159.177:9000/bucket/1769943435909_e0eeb515-d996-438a-9dfc-ffbb49fa564d_jvm-000001.jmx");
        System.out.println(tempAccessFileUrl);
    }

    @Test
    public void testReadRemoteFile(){
        String content = FileUtil.readRemoteFile("http://18.181.159.177:9000/bucket/1769943435909_e0eeb515-d996-438a-9dfc-ffbb49fa564d_jvm-000001.jmx?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minio_root%2F20260201%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260201T111257Z&X-Amz-Expires=3600&X-Amz-SignedHeaders=host&X-Amz-Signature=f50041207d23257a1a307116d847c6adca17e4cea18d4dfc65d7e21c8498c496");
        System.out.println(content);
    }

}
