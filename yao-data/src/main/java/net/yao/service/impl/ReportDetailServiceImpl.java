package net.yao.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.yao.dto.StressSampleResultDTO;
import net.yao.mapper.ReportDetailStressMapper;
import net.yao.model.ReportDetailStressDO;
import net.yao.service.ReportDetailService;
import net.yao.util.JsonUtil;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportDetailServiceImpl implements ReportDetailService {

    @Autowired
    private ReportDetailStressMapper reportDetailStressMapper;

    public void handleStressReportDetail(String topicContent){
        //通过 JsonUtil 将其“反序列化”，从一串枯燥的文本转成了一个有灵魂的 Java 对象 StressSampleResultDTO。
        StressSampleResultDTO  stressSampleResultDTO = JsonUtil.json2Obj(topicContent, StressSampleResultDTO.class);
        //DO (Data Object): 专门映射数据库表的实体类。
        ReportDetailStressDO reportDetailStressDO = SpringBeanUtil.copyProperties(stressSampleResultDTO, ReportDetailStressDO.class);

        reportDetailStressMapper.insert(reportDetailStressDO);
    }

}
