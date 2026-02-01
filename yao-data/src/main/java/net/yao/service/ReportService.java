package net.yao.service;

import net.yao.dto.ReportDTO;
import net.yao.req.ReportSaveReq;

public interface ReportService {

    ReportDTO save(ReportSaveReq req);
}
