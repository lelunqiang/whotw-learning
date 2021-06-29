package com.whotw.uaa.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whotw.common.data.CacheConstants;
import com.whotw.common.data.ResourceEndpoint;
import com.whotw.uaa.entity.ResourceEndpointEntity;
import com.whotw.uaa.mapper.ResourceEndpointRepository;
import com.whotw.uaa.rest.vo.QueryResourceVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author edison
 * @since 2019-10-18
 */
@Service
public class ResourceEndpointService extends ServiceImpl<ResourceEndpointRepository, ResourceEndpointEntity>{

    private static final Logger logger = LoggerFactory.getLogger(ResourceEndpointService.class);

    @Autowired
    private ResourceEndpointRepository repository;
    @Autowired
    private RedisTemplate redisTemplate;
    /*@Autowired
    private InstitutionProductsOverviewService institutionProductsOverviewService;
    @Autowired
    private FunctionResourceBindingService functionResourceBindingService;*/

    /**
     * pos默认值为0，idx默认值为1，是靠SQL语句自动递增，并在达到最大值9223372036854775807时自动重置idx为0,pos+1
     * 此处使用MYSQL自己的INSERT SELECT来避免分布式锁的问题，但是如果当前表是空，则无法使用，所以必须在启动时检查并插入一条初始化的记录
     */
    public void init(){
        repository.insertDefaultValue();
    }

    /**
     * 资源自动注册，如果已经注册，则返回其权限空间及权限位
     * @param desc 资源描述
     * @param method HTTP方法
     * @param url REST接口的URL
     * @return　对应的权限空间及权限位
     */
    @Transactional
    public Long[] register(String desc, String method, String url){
        return register(desc, method, url, ResourceEndpointEntity.TYPE_API);
    }

   /**
    * 资源自动注册，如果已经注册，则返回其权限空间及权限位
    * @param desc 资源描述
    * @param method HTTP方法
    * @param url REST接口的URL
    * @return　对应的权限空间及权限位
    */
    @Transactional
    public Long[] register(String desc, String method, String url, Integer type){
        Long[] result = new Long[2];
        ResourceEndpointEntity endpointEntity = findByMethodAndUrl(method, url);
        if(endpointEntity==null){
            endpointEntity = new ResourceEndpointEntity(desc, method, url, type);
            repository.insertWithGeneratedPermissionValue(endpointEntity);
            //插入后自动生成pos和idx，故需要再查一次
            endpointEntity = findByMethodAndUrl(method, url);
        }
        if(endpointEntity!=null){
            result[0] = endpointEntity.getPermissionPos();
            result[1] = endpointEntity.getPermissionIdx();
        }
        if(endpointEntity.getPermissionPos()> Integer.MAX_VALUE){
            logger.error("当前系统权限点已超过能容纳最大值，需要改进！");
            throw new IllegalStateException("当前系统权限点已超过能容纳最大值，需要改进");
        }
        // 清除缓存
        if(!ResourceEndpointEntity.TYPE_API.equals(type)){
            redisTemplate.delete(Arrays.asList(CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_PAGE,
                    CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_SERVICE,
                    CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_PAGE,
                    CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_SERVICE));
        }
        return result;
    }

    @CacheEvict(cacheNames = {
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_PAGE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_SERVICE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_PAGE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_SERVICE
    })
    /*@Transactional
    // API类型删除无效，服务启动时会自动注册，在前端拦截
    public void deleteResourceEndpoint(Long id){
        logger.warn("删除资源点: {}", id);
        removeById(id);
        functionResourceBindingService.removeByResourceId(id);
        institutionProductsOverviewService.batchRefreshDefaultPermission();
    }*/

    public ResourceEndpointEntity findByMethodAndUrl(String method, String url){
        return this.getOne(
                Wrappers
                    .<ResourceEndpointEntity>query()
                    .lambda()
                    .eq(StringUtils.isNotBlank(method), ResourceEndpointEntity::getMethod, method)
                    .eq(ResourceEndpointEntity::getUrl, url)
        );
    }

    public IPage<ResourceEndpointEntity> list(Page<ResourceEndpointEntity> page, QueryResourceVO queryResourceVO){
        String description = null;
        String url = null;
        String method = null;
        Integer type = null;
        boolean onlyRoot = true;
        if(queryResourceVO!=null){
            description = queryResourceVO.getDescription();
            url = queryResourceVO.getUrl();
            method = queryResourceVO.getMethod();
            type = queryResourceVO.getType();
        }
        return repository.list(page, description, url, method, type);
    }


    public List<ResourceEndpointEntity> listAll(QueryResourceVO queryResourceVO) {
        String description = null;
        String url = null;
        String method = null;
        Integer type = null;
        if(queryResourceVO!=null){
            description = queryResourceVO.getDescription();
            url = queryResourceVO.getUrl();
            method = queryResourceVO.getMethod();
            type = queryResourceVO.getType();
        }
        return repository.list(description, url, method, type);
    }

    public List<ResourceEndpoint> batchRegister(List<ResourceEndpoint> resourceEndpoints) {
        if(CollectionUtils.isEmpty(resourceEndpoints)) {
            logger.warn("资源点注册信息为空，忽略");
            return new ArrayList<>();
        }
        resourceEndpoints.forEach(e->{
            Long[] permission = register(e.getDescription(), e.getMethod(), e.getUrl(), e.getType());
            e.setPermissionPos(permission[0]);
            e.setPermissionIdx(permission[1]);
        });
        return resourceEndpoints;
    }

    /*@CacheEvict(cacheNames = {
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_PAGE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_SERVICE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_PAGE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_SERVICE
    })
    @Transactional
    public ResourceEndpointEntity add(ResourceEndpointEntity entity){
        Integer type = entity.getType();
        String method = entity.getMethod();
        String url = entity.getUrl();

        if(ResourceEndpointEntity.TYPE_API.equals(type)){
            throw new IllegalArgumentException("API类型资源点不允许新增");
        }
        ResourceEndpointEntity existOne = findByMethodAndUrl(method, url);
        if(existOne!=null)
            throw new IllegalArgumentException("资源方法和URL重复");

        repository.insertWithGeneratedPermissionValue(entity);
        //插入后自动生成pos和idx，故需要再查一次
        entity = findByMethodAndUrl(method, url);

        if(entity.getPermissionPos()> Integer.MAX_VALUE){
            logger.error("当前系统权限点已超过能容纳最大值，需要改进！");
            throw new IllegalStateException("当前系统权限点已超过能容纳最大值，需要改进");
        }
        // 如果改动的是匿名或者改为匿名，批量刷新机构拥有的功能权限
        if(entity.isAllowAnonymous())
            institutionProductsOverviewService.batchRefreshDefaultPermission();
        return entity;
    }*/
/*
    @CacheEvict(cacheNames = {
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_PAGE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_SERVICE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_PAGE,
            CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_SERVICE
    })
    @Transactional
    public void updateResourceInfo(ResourceEndpointEntity endpoint){
        if(ResourceEndpointEntity.TYPE_API.equals(endpoint.getType()))
           throw new IllegalArgumentException("API类型的资源不能修改");
        boolean updated = lambdaUpdate()
                            .eq(ResourceEndpointEntity::getId, endpoint.getId())
                            .set(endpoint.getType()!=null, ResourceEndpointEntity::getType, endpoint.getType())
                            .set(ResourceEndpointEntity::getDescription, endpoint.getDescription())
                            .set(ResourceEndpointEntity::getMethod, endpoint.getMethod())
                            .set(ResourceEndpointEntity::getUrl, endpoint.getUrl())
                            .set(ResourceEndpointEntity::isAllowAnonymous, endpoint.isAllowAnonymous())
                            .update();
        if(updated) {
            logger.info("更新资源{}信息成功", endpoint.getId());
            // 如果改动的是匿名或者改为匿名，批量刷新机构拥有的功能权限
            if(endpoint.isAllowAnonymous())
                institutionProductsOverviewService.batchRefreshDefaultPermission();
        }
    }*/

    public List<ResourceEndpointEntity> listAllByType(Integer type){
        if(type==null)
            return null;
        return lambdaQuery().eq(ResourceEndpointEntity::getType, type).list();
    }

    @Cacheable(cacheNames = CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_PAGE, unless = "#result==null")
    public List<ResourceEndpointEntity> listAllPageResourceEndpoints(){
        return listAllByType(ResourceEndpointEntity.TYPE_PAGE);
    }

    @Cacheable(cacheNames = CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_PAGE, unless = "#result==null")
    public Map<String, Long[]> getPageResourcePermissionMap(){
        List<ResourceEndpointEntity> pageEndpoints = listAllPageResourceEndpoints();
        if(CollectionUtils.isEmpty(pageEndpoints))
            return null;
        return pageEndpoints
                .stream()
                .collect(Collectors.toMap(
                        ResourceEndpointEntity::getUrl, r->new Long[]{r.getPermissionPos(), r.getPermissionIdx()}
                        )
                );
    }

    @Cacheable(cacheNames = CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_SERVICE, unless = "#result==null")
    public List<ResourceEndpointEntity> listAllServiceResourceEndpoints(){
        return listAllByType(ResourceEndpointEntity.TYPE_SERVICE);
    }

    @Cacheable(cacheNames = CacheConstants.CACHE_KEY_PREFIX_RESOURCE_PERMISSION_MAP_SERVICE, unless = "#result==null")
    public Map<String, Long[]> getServiceResourcePermissionMap(){
        List<ResourceEndpointEntity> pageEndpoints = listAllServiceResourceEndpoints();
        if(CollectionUtils.isEmpty(pageEndpoints))
            return null;
        return pageEndpoints
                .stream()
                .collect(Collectors.toMap(
                        ResourceEndpointEntity::getUrl, r->new Long[]{r.getPermissionPos(), r.getPermissionIdx()}
                        )
                );
    }

    /**
     * 查找所有可匿名访问的服务资源
     * @return
     */
    public List<ResourceEndpointEntity> listAllAnonymousServiceResourceEndpoints(){
        List<ResourceEndpointEntity> serviceEndpoints = listAllServiceResourceEndpoints();
        if(CollectionUtils.isEmpty(serviceEndpoints))
            return null;
        return serviceEndpoints.stream().filter(e->e.isAllowAnonymous()).collect(Collectors.toList());
    }
}
