package net.yao.controller.common;


import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.http.HttpServletResponse;
import net.yao.enums.BizCodeEnum;
import net.yao.util.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    @RequestMapping("/api/v1/test/login_form")
    public JsonData login(String mail,String password)
    {
        if(mail.startsWith("haha")){
            return JsonData.buildError("Account Error");
        }
        return JsonData.buildSuccess("mail="+mail+" password="+password);

    }



    /**
     * json提交 postman要提出json
     * @return
     */
    @PostMapping("/api/v1/test/pay_json")
    @ResponseBody
    public JsonData pay(@RequestBody Map<String,String> map) {
        String id = map.get("id");
        String amount = map.get("amount");
        return JsonData.buildSuccess("id="+id+",amount="+amount);
    }
    /**
     * json提交, 加上随机睡眠时间
     * @return
     */
    @PostMapping("/api/v1/test/pay_json_sleep")
    @ResponseBody
    public JsonData paySleep(@RequestBody Map<String,String> map) {
        try {
            int value = RandomUtil.randomInt(2000);
            TimeUnit.MICROSECONDS.sleep(value);
            String id = map.get("id");
            String amount = map.get("amount");
            return
                    JsonData.buildSuccess("id="+id+",amount="+amount+",sleep="+value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * get⽅式提交
     * @param id
     * @return
     */
    @GetMapping("/api/v1/test/query")
    @ResponseBody
    public JsonData queryDetail(Long id){
        return JsonData.buildSuccess("id="+id);
    }
    /**
     * get⽅式，随机睡眠时间
     * @param id
     * @return
     */
    @GetMapping("/api/v1/test/query_sleep")
    @ResponseBody
    public JsonData querySleep(Long id){
        try {

            int value = RandomUtil.randomInt(1000);
            TimeUnit.MICROSECONDS.sleep(value);
            return JsonData.buildSuccess("id="+id+",sleep time="+value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * get⽅式，id取模3是0后则http状态码500
     * @param id
     * @return
     */
    @GetMapping("/api/v1/test/query_error_code")
    @ResponseBody
    public JsonData queryError(Long id,  HttpServletResponse response){
        if(id % 3 == 0){
            response.setStatus(500);
        }
        return JsonData.buildSuccess("id="+id);
    }
}


