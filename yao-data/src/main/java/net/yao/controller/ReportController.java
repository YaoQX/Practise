package net.yao.controller;

import jakarta.annotation.Resource;
import net.yao.dto.ReportDTO;
import net.yao.req.ReportSaveReq;
import net.yao.service.ReportService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
