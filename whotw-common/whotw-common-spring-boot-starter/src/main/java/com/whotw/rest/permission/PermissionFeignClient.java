package com.whotw.rest.permission;

import com.whotw.common.data.ResourceEndpoint;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author EdisonXu
 * @date 2019-10-21
 */
@FeignClient(value = "http://service-uaa")  //为防止其他服务中需要实现同名的feignClient，这里手动加上http://前缀
public interface PermissionFeignClient {

    @PostMapping(value = "/management/resource-endpoints", headers = {"whotw_inner_access=1"})
    @ResponseBody
    Long[] register(@RequestParam("description") String description,
                    @RequestParam("method") String method,
                    @RequestParam("url")  String url,
                    @RequestParam("type") Integer type
                    );

    @PostMapping("/management/resource-endpoints/batch")
    @ResponseBody
    List<ResourceEndpoint> batchRegister(@RequestBody List<ResourceEndpoint> resourceEndpoints);
}
