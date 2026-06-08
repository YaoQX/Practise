package net.yao.req;

import lombok.Data;

@Data
public class AccountUpdateReq {

    private Long id;
    /**
     * 账号的状态
     */
    private Boolean enabled;
}
