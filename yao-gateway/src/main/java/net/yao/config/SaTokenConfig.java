package net.yao.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import net.yao.enums.BizCodeEnum;
import net.yao.enums.PermissionEnum;
import net.yao.util.JsonData;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaTokenConfig {

    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 1. 拦截所有进来的请求路径
                .addInclude("/**")
                // 2. 放行不需要登录的白名单
                .addExclude(
                        "/account-service/api/v1/account/login",
                        "/account-service/api/v1/account/register"
                )
                 // 3. 核心鉴权逻辑：网关会直接去 Redis 里查对应的 Token 状态
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 第一步，必须是登录用户
                    SaRouter.match("/**", r -> StpUtil.checkLogin());
                    // 权限认证 -- 不同模块, 校验不同权限
                    //任何权限都可以
                    SaRouter.match("/*/api/v1/*/list","/*/api/v1/*/find","/*/api/v1/*/page","/*/api/v1/*/get_temp_url","/*/api/v1/*/findLoginAccountRole","/*/api/v1/*/export")
                            .check(r -> {
                                StpUtil.checkPermissionOr( PermissionEnum.PROJECT_READ_WRITE.name(), PermissionEnum.PROJECT_AUTH.name(), PermissionEnum.PROJECT_READ_ONLY.name());
                            });
                    //需要 读写权限 或 授权权限
                    SaRouter.match(
                                    "/*/api/v1/*/save",
                                    "/*/api/v1/*/update",
                                    "/*/api/v1/*/del",
                                    "/*/api/v1/*/execute",
                                    "/*/api/v1/*/upload"
                            )
                            .check(r -> {
                                StpUtil.checkPermissionOr(PermissionEnum.PROJECT_READ_WRITE.name(), PermissionEnum.PROJECT_AUTH.name());

                            });
                    //给别人授权，需要管理员
                    SaRouter.match("/*/api/permit/v1/**").check(r -> {StpUtil.checkPermissionOr(PermissionEnum.PROJECT_AUTH.name());});
                })
                 //  4.异常处理方法（每次 setAuth 出现异常时进入）
                .setError(e -> {
                    // 万一拦截了，必须在这里重新补上跨域头，否则前端浏览器会直接报 CORS 跨域错误
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "*")
                            .setHeader("Access-Control-Allow-Headers", "*")
                            .setHeader("Access-Control-Max-Age", "3600")
                            .setHeader("Content-Type", "application/json;charset=UTF-8"); // 防止中文乱码

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("--------OPTIONS: Preflight requests, do not process.-------"))
                            .back();

                    // 统一格式返回给前端：不要用 SaResult，改成跟业务完全一致的 JsonData
                    if (e instanceof NotLoginException) {
                        return JsonData.buildResult(BizCodeEnum.AUTH_NOT_LOGIN);
                    }
                    if (e instanceof NotPermissionException) {
                        return JsonData.buildResult(BizCodeEnum.AUTH_NO_PERMISSION);
                    }

                    // 兜底的其他内部未知崩溃异常
                    return JsonData.buildResult(BizCodeEnum.SERVER_ERROR);
                });
    }
}
