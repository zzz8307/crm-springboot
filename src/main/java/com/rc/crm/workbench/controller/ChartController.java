package com.rc.crm.workbench.controller;

import com.rc.crm.workbench.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rc
 */
@Controller
@RequestMapping("/workbench/chart")
public class ChartController {

    @Resource
    private TransactionService transactionService;

    @GetMapping("/transaction/index")
    public String tranChart() {
        return "workbench/chart/transaction/index";
    }

    @GetMapping("/transaction/getChart")
    @ResponseBody
    public Object getChart() {
        long total = transactionService.countAll();
        List<Map<String, Object>> dataList = transactionService.getListCountByStage();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList", dataList);
        return map;
    }
}
