package net.yao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.yao.dto.AccountDTO;
import net.yao.dto.RoleDTO;
import net.yao.model.RoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<RoleDO> {

    List<RoleDTO> listRoleWithPermission();

    AccountDTO findAccountWithRoleAndPermission(@Param("accountId") Long accountId);

}
