package net.yao.feign;

import net.yao.req.ReportSaveReq;
import net.yao.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 并在上面加上注解。你不需要写实现类，Feign 会自动帮你生成实现。
 */
@FeignClient("data-service")
public interface ReportFeignService {
    /**
     * 初始化测试报告
     */
    @RequestMapping("/api/v1/report/save")
    JsonData save(ReportSaveReq req);
}
