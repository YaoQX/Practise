package net.yao.service;

import net.yao.dto.ReportDTO;
import net.yao.req.ReportDelReq;
import net.yao.req.ReportSaveReq;
import net.yao.req.ReportUpdateReq;

public interface ReportService {

    ReportDTO save(ReportSaveReq req);

    /**
     * 更新状态
     * @param req
     */
    void updateReportState(ReportUpdateReq req);

//    /**
//     * 删除报告
//     * @param req
//     * @return
//     */
//    int delete(ReportDelReq req);
}
