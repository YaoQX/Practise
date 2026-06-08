package net.yao.service;

import net.yao.dto.AccountDTO;
import net.yao.req.AccountDelReq;
import net.yao.req.AccountLoginReq;
import net.yao.req.AccountRegisterReq;
import net.yao.req.AccountUpdateReq;

public interface AccountService {

    int del(AccountDelReq req);

    int update(AccountUpdateReq req);

    int register(AccountRegisterReq req);

    AccountDTO login(AccountLoginReq req);
}
