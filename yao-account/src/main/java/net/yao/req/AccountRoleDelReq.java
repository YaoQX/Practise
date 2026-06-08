package net.yao.req;

import lombok.Data;

@Data
public class AccountRoleDelReq {

    private Long accountId;

    private Long roleId;
}
