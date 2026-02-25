package net.yao.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.yao.config.KafkaTopicConfig;
import net.yao.dto.ReportDTO;
import net.yao.req.ReportSaveReq;
import net.yao.req.ReportUpdateReq;
import net.yao.service.ReportService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/report")
public class ReportController
{
    @Autowired
    private ReportService reportService;

    @PostMapping("/save")
    public JsonData save(@RequestBody ReportSaveReq req){

        ReportDTO reportDTO = reportService.save(req);

        return JsonData.buildSuccess(reportDTO);
    }

    @PostMapping("/update")
    public JsonData update(@RequestBody ReportUpdateReq req){

        reportService.updateReportState(req);

        return JsonData.buildSuccess();
    }

}
