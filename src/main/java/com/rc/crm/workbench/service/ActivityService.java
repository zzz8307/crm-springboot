package com.rc.crm.workbench.service;

import com.github.pagehelper.PageInfo;
import com.rc.crm.workbench.domain.Activity;
import com.rc.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @author rc
 */
public interface ActivityService {
    int addActivity(Activity activity, String createBy);

    PageInfo<Activity> getActivityByConditions(Integer pageNo, Integer pageSize, Activity activity);

    boolean deleteActivityById(String[] id);

    Activity getActivityById(String id);

    int updateActivity(Activity activity, String editBy);

    List<ActivityRemark> getActivityRemarkByActivityId(String id);

    boolean deleteActivityRemarkById(String id);

    int addActivityRemark(String createBy, ActivityRemark remark);

    int updateActivityRemark(String editBy, ActivityRemark remark);

    List<Activity> getActivityByClueId(String id);

    List<Activity> getActivityListExcludeBound(String clueId, String query);

    List<Activity> getActivityList(String query);
}
