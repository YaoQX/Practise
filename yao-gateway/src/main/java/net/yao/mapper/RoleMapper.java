package net.yao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.yao.model.RoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<RoleDO> {

    List<String> findRoleCodeList(@Param("accountId") Long accountId);

}
