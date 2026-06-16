package net.yao.gatewaymapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import net.yao.model.PermissionDO;

import java.util.List;

public interface PermissionMapper extends BaseMapper<PermissionDO> {

    List<String> findPermissionCodeList(@Param("accountId") Long accountId);

}
