package net.yao.req;

import lombok.Data;

@Data
public class AccountRoleAddReq {

    private Long accountId;

    private Long roleId;
}
