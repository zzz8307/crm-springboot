package com.rc.crm.workbench.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author rc
 */
@Controller
@RequestMapping("/workbench")
public class WorkbenchController {

    @GetMapping("/index")
    public String index() {
        return "workbench/index";
    }

    @GetMapping("/main/index")
    public String mainIndex() {
        return "workbench/main/index";
    }
}
