package net.yao.service.impl;

import net.yao.dto.ReportDTO;
import net.yao.mapper.ReportMapper;
import net.yao.model.ReportDO;
import net.yao.req.ReportSaveReq;
import net.yao.service.ReportService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    public ReportDTO save(ReportSaveReq req) {
        ReportDO reportDO = SpringBeanUtil.copyProperties(req, ReportDO.class);
        reportMapper.insert(reportDO);

        ReportDTO reportDTO = ReportDTO.builder().id(reportDO.getId())
                .projectId(reportDO.getProjectId())
                .name(reportDO.getName()).build();

        return reportDTO;

    }

}
