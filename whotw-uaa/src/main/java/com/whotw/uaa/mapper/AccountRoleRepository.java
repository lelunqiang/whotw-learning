package com.whotw.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whotw.uaa.entity.AccountRoleEntity;
import com.whotw.uaa.rest.vo.AccountRoleVO;
import com.whotw.uaa.rest.vo.FunctionAndResourceEndpoint;
import com.whotw.uaa.rest.vo.QueryRoleVO;
import com.whotw.uaa.rest.vo.RolePermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author edison
 * @since 2019-10-18
 */
@Mapper
public interface AccountRoleRepository extends BaseMapper<AccountRoleEntity> {

    IPage<AccountRoleVO> listAll(Page<FunctionAndResourceEndpoint> page,
                                 @Param("onlyRoot") boolean onlyRoot,
                                 @Param("query") QueryRoleVO queryRoleVO);

    List<AccountRoleVO> listAll(@Param("onlyRoot") boolean onlyRoot,
                                @Param("query") QueryRoleVO queryRoleVO);

    List<AccountRoleVO> findByRootId(List<Long> rootIds);

    List<AccountRoleEntity> findRoleByAccountId(Long accountId);

    List<AccountRoleEntity> findDefaultRoles(List<String> list);

    void batchUpdateRootId(@Param("rootId") Long rootId,
                           @Param("children") List<Long> children);

    List<RolePermissionVO>  listPermissionByRoleId(@Param("roleId") Long roleId);

    List<AccountRoleEntity> listSimpleInfoOfDefaultRoles();

    List<AccountRoleVO> listWithFunctionsAndResources(@Param("name") String roleName);
}
