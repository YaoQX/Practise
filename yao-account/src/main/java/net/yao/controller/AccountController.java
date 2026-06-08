package net.yao.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import net.yao.dto.AccountDTO;
import net.yao.req.AccountDelReq;
import net.yao.req.AccountLoginReq;
import net.yao.req.AccountRegisterReq;
import net.yao.req.AccountUpdateReq;
import net.yao.service.AccountService;
import net.yao.service.RoleService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    /**
     * 根据id删除
     */
    @PostMapping("del")
    public JsonData del(@RequestBody AccountDelReq req) {
        int rows = accountService.del(req);
        return JsonData.buildSuccess(rows);
    }

    /**
     * 更新账号状态
     */
    @PostMapping("update")
    public JsonData update(@RequestBody AccountUpdateReq req) {
        int rows = accountService.update(req);
        return JsonData.buildSuccess(rows);

    }

    /**
     * 注册接口
     */
    @PostMapping("register")
    public JsonData register(@RequestBody AccountRegisterReq req) {
        int rows = accountService.register(req);
        return JsonData.buildSuccess(rows);
    }

    /**
     * 登录接口
     * JWT
     * @return
     */
    @PostMapping("login")
    public JsonData login(@RequestBody AccountLoginReq req) {

        AccountDTO accountDTO = accountService.login(req);
        if (accountDTO != null) {
            //登录
            StpUtil.login(accountDTO.getId());
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return JsonData.buildSuccess(tokenInfo);
        } else {
            return JsonData.buildError("Login Error");
        }
    }

    /**
     * 退出登录接口
     */
    @GetMapping("logout")
    public JsonData logout() {
        StpUtil.logout();
        return JsonData.buildSuccess();
    }


    /**
     * 根据登录账号获取角色信息
     * @return
     */
    @GetMapping("findLoginAccountRole")
    public JsonData findLoginAccountRole(){
        Long accountId = Long.parseLong(StpUtil.getLoginId().toString());
        AccountDTO accountDTO = roleService.getAccountWithRoleByAccountId(accountId);
        return JsonData.buildSuccess(accountDTO);
    }
}
