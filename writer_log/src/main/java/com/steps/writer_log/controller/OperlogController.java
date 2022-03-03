package com.steps.writer_log.controller;

import com.steps.writer_log.annotation.SysLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2021-09-12 17:17
 */
@RestController
@RequestMapping("log")
public class OperlogController {

    @GetMapping("/test")
    @SysLog(title = "测试", value = "测试")
    public String test() {

        return "helo";
    }


}
