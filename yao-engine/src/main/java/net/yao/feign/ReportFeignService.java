package net.yao.feign;

import net.yao.req.ReportSaveReq;
import net.yao.req.ReportUpdateReq;
import net.yao.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 并在上面加上注解。你不需要写实现类，Feign 会自动帮你生成实现。
 */
@FeignClient("data-service")
public interface ReportFeignService {
    /**
     * 初始化测试报告接口
     */
    @PostMapping("/api/v1/report/save")
    JsonData save(ReportSaveReq req);

    /**
     * 更新测试报告接口
     */
    @PostMapping("/api/v1/report/update")
    void update(@RequestBody ReportUpdateReq req);
}
