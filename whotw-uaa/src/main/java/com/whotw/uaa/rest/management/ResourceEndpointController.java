package com.whotw.uaa.rest.management;

import com.whotw.common.data.ResourceEndpoint;
import com.whotw.mysql.utils.PageUtil;
import com.whotw.uaa.entity.ResourceEndpointEntity;
import com.whotw.uaa.rest.vo.QueryResourceVO;
import com.whotw.uaa.security.service.ResourceEndpointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author EdisonXu
 * @date 2019-10-20
 */
@Api(tags = "资源管理接口")
@RestController
@RequestMapping("/management/resource-endpoints")
public class ResourceEndpointController {

    @Autowired
    private ResourceEndpointService endpointService;

    @ApiOperation("注册资源并返回权限编号")
    @PostMapping
    @ResponseBody
    public Long[] register(@ApiParam("资源描述") @RequestParam String description,
                           @ApiParam("HTTP方法") @RequestParam String method,
                           @ApiParam("URL") @RequestParam  String url,
                           @ApiParam("类型") @RequestParam Integer type){
        return endpointService.register(description, method, url, type);
    }

    @PostMapping("/batch")
    @ResponseBody
    public List<ResourceEndpoint> batchRegister(@RequestBody List<ResourceEndpoint> resourceEndpoints){
        return endpointService.batchRegister(resourceEndpoints);
    }
}
