package net.yao.mapper;

import net.yao.model.RoleDO;
import net.yao.dto.AccountDTO;
import net.yao.dto.RoleDTO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<RoleDO> {

    List<RoleDTO> listRoleWithPermission();

    AccountDTO findAccountWithRoleAndPermission(@Param("accountId") Long accountId);

}
