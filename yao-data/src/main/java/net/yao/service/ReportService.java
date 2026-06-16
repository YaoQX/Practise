package net.yao.service;

import net.yao.controller.req.ReportExportReq;
import net.yao.dto.ReportDTO;
import net.yao.dto.ReportExcelDTO;
import net.yao.req.ReportDelReq;
import net.yao.req.ReportSaveReq;
import net.yao.req.ReportUpdateReq;

import java.util.List;

public interface ReportService {

    ReportDTO save(ReportSaveReq req);

    /**
     * 更新状态
     * @param req
     */
    void updateReportState(ReportUpdateReq req);

    /**
     * 删除报告
     * @param req
     * @return
     */
    int delete(ReportDelReq req);

    /**
     * 导出报告 只负责查
     * @param req
     * @return
     */
    List<ReportExcelDTO> exportReport(ReportExportReq req);
}
