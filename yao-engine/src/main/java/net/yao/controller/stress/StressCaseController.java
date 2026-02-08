package net.yao.controller.stress;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.model.StressCaseDO;
import net.yao.req.stress.StressCaseDelReq;
import net.yao.req.stress.StressCaseSaveReq;
import net.yao.req.stress.StressCaseUpdateReq;
import net.yao.service.stress.StressCaseService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stress_case")
public class StressCaseController {
    @Autowired
    private StressCaseService stressCaseService;

    @RequestMapping("/find")
    public JsonData findById(@RequestParam("projectId") Long projectId, @RequestParam("id") Long caseId){
        return JsonData.buildSuccess(stressCaseService.findById(projectId, caseId));
    }

    @PostMapping("/del")
    public JsonData delete(@RequestBody StressCaseDelReq req){
        return JsonData.buildSuccess(stressCaseService.delete(req.getProjectId(),req.getId()));
    }

    @PostMapping("/save")
    public JsonData save(@RequestBody StressCaseSaveReq req){
        return JsonData.buildSuccess(stressCaseService.save(req));
    }

    @PostMapping("/update")
    public JsonData update(@RequestBody StressCaseUpdateReq req){
        return JsonData.buildSuccess(stressCaseService.update(req));
    }

    /**
     * 执⾏⽤例
     *
     * @return
     */
    @GetMapping("/execute")
    public JsonData execute(@RequestParam("projectId")Long projectId,@RequestParam("id") Long caseId) {
//        stressCaseService.execute(projectId,caseId);
//        return JsonData.buildSuccess();
        try {
            System.out.println("Interface execution start ：" + projectId + ", " + caseId);
            stressCaseService.execute(projectId, caseId);
            return JsonData.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace(); // 在控制台打印具体的红色堆栈信息
            return JsonData.buildError("Error：" + e.getMessage());
        }
    }

//    @GetMapping("/hello")
//    public String hello() {
//        return "I am alive";
//    }



}
