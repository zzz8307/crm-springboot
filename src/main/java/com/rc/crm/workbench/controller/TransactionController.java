package com.rc.crm.workbench.controller;

import com.rc.crm.settings.domain.User;
import com.rc.crm.settings.service.UserService;
import com.rc.crm.workbench.domain.Tran;
import com.rc.crm.workbench.domain.TranHistory;
import com.rc.crm.workbench.service.CustomerService;
import com.rc.crm.workbench.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author rc
 */
@Controller
@RequestMapping("/workbench/transaction")
public class TransactionController {

    @Resource
    private TransactionService transactionService;

    @Resource
    private UserService userService;

    @Resource
    private CustomerService customerService;

    @GetMapping("/index")
    public String index(Model model) {
        List<Tran> tranList = transactionService.getTransList();
        model.addAttribute("tranList", tranList);
        return "workbench/transaction/index";
    }

    @GetMapping("/save")
    public String save(Model model) {
        List<User> ownerList = userService.getAllUsers();
        model.addAttribute("ownerList", ownerList);
        return "workbench/transaction/save";
    }

    @GetMapping("/getCustomerName")
    @ResponseBody
    public Object getCustomerName(String name) {
        return customerService.getCustomerName(name);
    }

    @PostMapping("/addTran")
    public String addTran(HttpServletRequest request, String customerName, Tran tran) {
        String createBy = ((User) request.getSession().getAttribute("user")).getId();
        transactionService.addTransaction(tran, customerName, createBy);
        return "redirect:index";
    }

    @GetMapping("/detail")
    public String detail(Model model, String id) {
        Tran tran = transactionService.getTran(id);
        model.addAttribute("tran", tran);
        return "workbench/transaction/detail";
    }

    @GetMapping("/loadHistory")
    public String loadHistory(Model model, String id) {
        List<TranHistory> tranHistoryList = transactionService.getTranHistoryList(id);
        model.addAttribute("tranHistoryList", tranHistoryList);
        return "workbench/transaction/detail::history-table";
    }

    @PostMapping("/changeStage")
    public String changeStage(HttpServletRequest request, String id, String stage) {
        String editBy = ((User) request.getSession().getAttribute("user")).getId();
        transactionService.changeStage(id, stage, editBy);
        Tran tran = transactionService.getTran(id);
        request.setAttribute("tran", tran);
        return "workbench/transaction/detail::detail-form";
    }
}
