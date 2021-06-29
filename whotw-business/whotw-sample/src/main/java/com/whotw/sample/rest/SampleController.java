package com.whotw.sample.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whotw
 * @description SampleController
 * @date 2021/5/10 20:41
 */
@Api(tags = "项目模板测试")
@RestController
@RequestMapping("/api/sample")
public class SampleController {
    @ApiOperation("模板测试接口")
    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
