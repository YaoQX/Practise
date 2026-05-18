package net.yao.controller;//package net.xdclass.controller.satoken;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import net.yao.util.JsonData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TestController {
    // 会话登录接口,多次登录返回token相同，配置文件那边配置
    @RequestMapping("login")
    public JsonData doLogin(String name, String pwd) {
        // 第1步：比对前端提交的账号名称、密码
        if("yao".equals(name) && "123456".equals(pwd)) {

            // 第2步：根据账号id，进行登录
            //检查此账号是否之前已有登录；
            //为账号生成 Token 凭证与 Session 会话；
            //记录 Token 活跃时间；
            //通知全局侦听器，xx 账号登录成功；
            // 将 Token 注入到请求上下文；
            StpUtil.login(10001);

            // 第3步，获取 Token  相关参数, StpUtil.login(id) 方法利用了 Cookie 自动注入的特性，省略手写返回 token 的代码
            // 可以手动获取相关Token信息返回
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return JsonData.buildSuccess(tokenInfo);
        }
        return JsonData.buildError("登录失败");
    }

    @RequestMapping("logout")
    public JsonData doLogout() {
        // 第1步：根据当前登录账号，进行登出
        //删除 Token 活跃时间；
        //通知全局侦听器，xx 账号登出成功；
        // 将 Token 注销掉；
        StpUtil.logout();
        return JsonData.buildSuccess();
    }

    @RequestMapping("isLogin")
    public JsonData isLogin() {
        // 获取当前会话是否已经登录，返回true=已登录，false=未登录
        boolean login = StpUtil.isLogin();
        if(login){
            Object loginId = StpUtil.getLoginId();
            System.out.println("当前登录账号：" + loginId);
        }
        return JsonData.buildSuccess(login);
    }









}
