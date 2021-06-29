package com.whotw.uaa.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whotw
 * @description DemoRest
 * @date 2021/5/5 23:20
 */
@Api(tags = "测试实例")
@RestController
@RequestMapping("/api/demo")
public class DemoController {
    @ApiOperation("项目初始化测试接口")
    @GetMapping("/test")
    public String test() {
        return "success";
    }

    @ApiOperation("项目初始化测试接口2")
    @GetMapping("/test2")
    public String test2() {
        return "success";
    }
}
