package com.whotw.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whotw.uaa.entity.ResourceEndpointEntity;
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
public interface ResourceEndpointRepository extends BaseMapper<ResourceEndpointEntity> {

    void insertDefaultValue();
    void insertWithGeneratedPermissionValue(ResourceEndpointEntity entity);
    IPage<ResourceEndpointEntity> list(Page page,
                                       @Param("description") String description,
                                       @Param("url") String url,
                                       @Param("method") String method,
                                       @Param("type") Integer type);

    List<ResourceEndpointEntity> findByFunctionId(Long id);

    List<ResourceEndpointEntity> list(@Param("description") String description, @Param("url") String url, @Param("method") String method,
                                      @Param("type") Integer type);
}
