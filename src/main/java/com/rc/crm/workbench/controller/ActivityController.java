package com.rc.crm.workbench.controller;

import com.rc.crm.settings.domain.User;
import com.rc.crm.settings.service.UserService;
import com.rc.crm.workbench.domain.Activity;
import com.rc.crm.workbench.domain.ActivityRemark;
import com.rc.crm.workbench.service.ActivityService;
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
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource
    private UserService userService;

    @Resource
    private ActivityService activityService;

    private static final String SUCCESS_KEY = "success";

    @GetMapping("/index")
    public String index() {
        return "workbench/activity/index";
    }

    @GetMapping("/detail")
    public String detail(Model model, String id) {
        Activity activity = activityService.getActivityById(id);
        User owner = userService.getUserById(activity.getOwner());
        User createBy = userService.getUserById(activity.getCreateBy());
        User editBy = userService.getUserById(activity.getEditBy());

        model.addAttribute("activity", activity);
        model.addAttribute("owner", owner);
        model.addAttribute("createBy", createBy);
        model.addAttribute("editBy", editBy);
        return "workbench/activity/detail";
    }

    @GetMapping("/ownerList")
    public String ownerList(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);
        // 局部刷新，return "视图名称::fragment名称"
        return "workbench/activity/index::create-div-owner";
    }

    @PostMapping("/submit")
    @ResponseBody
    public Object submit(HttpServletRequest request, Activity activity) {
        String createBy = ((User) request.getSession().getAttribute("user")).getId();
        Map<String, Object> result = new HashMap<>(1);

        int inserted = activityService.addActivity(activity, createBy);
        if (inserted == 0) {
            result.put(SUCCESS_KEY, false);
            return result;
        }
        result.put(SUCCESS_KEY, true);
        return result;
    }

    @GetMapping("/loadRecordList")
    @ResponseBody
    public Object loadRecordList(Integer pageNo, Integer pageSize, Activity activity) {
        return activityService.getActivityByConditions(pageNo, pageSize, activity);
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam String[] id) {
        Map<String, Object> result = new HashMap<>(1);
        boolean success = activityService.deleteActivityById(id);
        result.put(SUCCESS_KEY, success);
        return result;
    }

    @GetMapping("/loadRecordById")
    public String loadRecordById(Model model, String id) {
        Activity activity = activityService.getActivityById(id);
        List<User> userList = userService.getAllUsers();
        model.addAttribute("activity", activity);
        model.addAttribute("userList", userList);
        return "workbench/activity/index::edit-div";
    }

    @PostMapping("/update")
    @ResponseBody
    public Object update(HttpServletRequest request, Activity activity) {
        String editBy = ((User) request.getSession().getAttribute("user")).getId();
        Map<String, Object> result = new HashMap<>(1);

        int updated = activityService.updateActivity(activity, editBy);
        if (updated == 0) {
            result.put(SUCCESS_KEY, false);
            return result;
        }
        result.put(SUCCESS_KEY, true);
        return result;
    }

    @GetMapping("/activityRemark")
    public String activityRemark(Model model, String id) {
        List<ActivityRemark> remarkList = activityService.getActivityRemarkByActivityId(id);
        model.addAttribute("remarkList", remarkList);
        return "workbench/activity/detail::remark-detail-div";
    }

    @PostMapping("/deleteRemark")
    @ResponseBody
    public Object deleteRemark(String id) {
        Map<String, Object> result = new HashMap<>(1);
        boolean success = activityService.deleteActivityRemarkById(id);
        result.put(SUCCESS_KEY, success);
        return result;
    }

    @PostMapping("/submitRemark")
    @ResponseBody
    public Object submitRemark(HttpServletRequest request, ActivityRemark remark) {
        String createBy = ((User) request.getSession().getAttribute("user")).getId();
        Map<String, Object> result = new HashMap<>(1);

        int inserted = activityService.addActivityRemark(createBy, remark);
        if (inserted == 0) {
            result.put(SUCCESS_KEY, false);
            return result;
        }
        result.put(SUCCESS_KEY, true);
        return result;
    }

    @PostMapping("/updateRemark")
    @ResponseBody
    public Object updateRemark(HttpServletRequest request, ActivityRemark remark) {
        String editBy = ((User) request.getSession().getAttribute("user")).getId();
        Map<String, Object> result = new HashMap<>(1);

        int updated = activityService.updateActivityRemark(editBy, remark);
        if (updated == 0) {
            result.put(SUCCESS_KEY, false);
            return result;
        }
        result.put(SUCCESS_KEY, true);
        return result;
    }
}
