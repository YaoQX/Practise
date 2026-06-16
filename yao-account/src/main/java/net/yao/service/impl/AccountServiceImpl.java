package net.yao.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.yao.dto.AccountDTO;
import net.yao.accountmapper.AccountMapper;
import net.yao.accountmapper.SocialAccountMapper;
import net.yao.model.AccountDO;
import net.yao.model.SocialAccountDO;
import net.yao.req.AccountDelReq;
import net.yao.req.AccountLoginReq;
import net.yao.req.AccountRegisterReq;
import net.yao.req.AccountUpdateReq;
import net.yao.service.AccountService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private SocialAccountMapper socialAccountMapper;

    public int del(AccountDelReq req)
    {
        int rows = accountMapper.deleteById(req.getId());
        //顺便删除social_account
        if(rows>0)
        {
            LambdaQueryWrapper<SocialAccountDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SocialAccountDO::getAccountId, req.getId());
            socialAccountMapper.delete(queryWrapper);

        }
        return rows;
    }


    public int update(AccountUpdateReq req) {
        AccountDO accountDO = new AccountDO();
        accountDO.setId(req.getId());
        accountDO.setIsDisabled(req.getEnabled());
        return accountMapper.updateById(accountDO);
    }

    /**
     * 用 Sa-Token 自带的加密工具 SaSecureUtil.md5() 把前端传来的密码进行了加密，然后去子表里撞库。
     * 子表里存了一个关键字段 account_id。一旦子表验证通过，立马拿着这个 ID 去主表里把这个人的“核心档案（用户名、头像、创建时间等）”捞出来。
     */
    public AccountDTO login(AccountLoginReq req)
    {

        String md5Credential = SecureUtil.md5(req.getCredential());

        LambdaQueryWrapper<SocialAccountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SocialAccountDO::getIdentifier, req.getIdentifier())
                .eq(SocialAccountDO::getIdentityType, req.getIdentityType())
        .eq(SocialAccountDO::getCredential, md5Credential).last("limit 1");

        SocialAccountDO socialAccountDO = socialAccountMapper.selectOne(queryWrapper);

        if(socialAccountDO!=null)
        {
            //查询主账号
            AccountDO accountDO = accountMapper.selectById(socialAccountDO.getAccountId());
            //不为空，且不是冻结状态
            if(accountDO!=null && !accountDO.getIsDisabled()){
                return SpringBeanUtil.copyProperties(accountDO, AccountDTO.class);
            }

        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int register(AccountRegisterReq req)
    {
        //先查询账号子表有没记录，没记录才插入主账号，然后再插入子表

        LambdaQueryWrapper<SocialAccountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SocialAccountDO::getIdentifier, req.getIdentifier())
                .eq(SocialAccountDO::getIdentityType, req.getIdentityType())
                .last("limit 1");
        SocialAccountDO oldsocialAccountDO = socialAccountMapper.selectOne(queryWrapper);
        if(oldsocialAccountDO==null)
        {
            //创建主账号
            AccountDO accountDO = new AccountDO();
            accountDO.setUsername(req.getUsername());
            accountDO.setIsDisabled(true);
            accountMapper.insert(accountDO);

            //创建子账号
            SocialAccountDO socialAccountDO = new SocialAccountDO();
            socialAccountDO.setAccountId(accountDO.getId());
            if(StringUtils.isNotBlank(req.getCredential())){
                //Sa-Token 提供的安全工具类 加密
                socialAccountDO.setCredential(SaSecureUtil.md5(req.getCredential()));
            }
            socialAccountDO.setIdentifier(req.getIdentifier());
            socialAccountDO.setIdentityType(req.getIdentityType());
            return socialAccountMapper.insert(socialAccountDO);



        }
        else {
            log.error("The account's unique identifier already exists:{}",req);
            return 0;
        }



    }

}
