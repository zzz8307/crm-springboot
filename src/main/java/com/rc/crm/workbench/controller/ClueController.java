package com.rc.crm.workbench.controller;

import com.rc.crm.settings.domain.User;
import com.rc.crm.settings.service.UserService;
import com.rc.crm.workbench.domain.Activity;
import com.rc.crm.workbench.domain.Clue;
import com.rc.crm.workbench.domain.Tran;
import com.rc.crm.workbench.service.ActivityService;
import com.rc.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rc
 */
@Controller
@RequestMapping("/workbench/clue")
public class ClueController {

    @Resource
    private UserService userService;

    @Resource
    private ClueService clueService;

    @Resource
    private ActivityService activityService;

    private static final String SUCCESS_KEY = "success";

    @GetMapping("/index")
    public String index(Model model) {
        List<Clue> clueList = clueService.getClues();
        model.addAttribute("clueList", clueList);
        return "workbench/clue/index";
    }

    @GetMapping("/detail")
    public String detail(Model model, String id) {
        Clue clue = clueService.getClueById(id);
        model.addAttribute("clue", clue);
        return "workbench/clue/detail";
    }

    @GetMapping("/convert")
    public String convert(Model model, String id, String fullname, String appellation, String company, String owner) {
        model.addAttribute("id", id);
        model.addAttribute("fullname", fullname);
        model.addAttribute("appellation", appellation);
        model.addAttribute("company", company);
        model.addAttribute("owner", owner);
        return "workbench/clue/convert";
    }

    @GetMapping("/ownerList")
    public String ownerList(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);
        // 局部刷新，return "视图名称::fragment名称"
        return "workbench/clue/index::create-div-owner";
    }

    @PostMapping("/submit")
    @ResponseBody
    public Object submit(HttpServletRequest request, Clue clue) {
        String createBy = ((User) request.getSession().getAttribute("user")).getId();
        Map<String, Object> result = new HashMap<>(1);

        int inserted = clueService.addClue(clue, createBy);
        result.put(SUCCESS_KEY, inserted != 0);

        return result;
    }

    @GetMapping("/activity")
    public String activity(Model model, String id) {
        List<Activity> activityList = activityService.getActivityByClueId(id);
        model.addAttribute("activityList", activityList);
        return "workbench/clue/detail::activity-div";
    }

    @PostMapping("/unbind")
    @ResponseBody
    public Object unbind(String id) {
        boolean success = clueService.deleteClueById(id);
        Map<String, Object> result = new HashMap<>(1);
        result.put(SUCCESS_KEY, success);
        return result;
    }

    @GetMapping("/detailActivityList")
    public String activityListDetail(Model model, String clueId, String query) {
        List<Activity> activityList = activityService.getActivityListExcludeBound(clueId, query);
        model.addAttribute("activityList", activityList);
        return "workbench/clue/detail::bind-activity-table";
    }

    @GetMapping("/convertActivityList")
    public String activityListConvert(Model model, String query) {
        List<Activity> activityList = activityService.getActivityList(query);
        model.addAttribute("activityList", activityList);
        return "workbench/clue/convert::activity-table-div";
    }

    @PostMapping("/bind")
    @ResponseBody
    public Object bind(@RequestParam String[] id, String clueId) {
        Map<String, Object> result = new HashMap<>(1);
        boolean success = clueService.bindActivity(id, clueId);
        result.put(SUCCESS_KEY, success);
        return result;
    }

    @PostMapping("/converter")
    @ResponseBody
    public Object converter(HttpServletRequest request, String clueId, Boolean isCreateTransaction, Tran tran) {
        Map<String, Object> result = new HashMap<>(1);
        String createBy = ((User) request.getSession().getAttribute("user")).getId();
        boolean success = clueService.convert(clueId, isCreateTransaction, tran, createBy);
        result.put(SUCCESS_KEY, success);
        return result;
    }
}
