package com.rc.crm.workbench.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.rc.crm.settings.dao.UserMapper;
import com.rc.crm.util.UUIDUtil;
import com.rc.crm.workbench.dao.ActivityMapper;
import com.rc.crm.workbench.dao.ActivityRemarkMapper;
import com.rc.crm.workbench.dao.ClueActivityRelationMapper;
import com.rc.crm.workbench.domain.*;
import com.rc.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author rc
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private ActivityRemarkMapper activityRemarkMapper;

    @Resource
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public int addActivity(Activity activity, String createBy) {
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(new Date());
        activity.setCreateBy(createBy);
        return activityMapper.insert(activity);
    }

    @Override
    public PageInfo<Activity> getActivityByConditions(Integer pageNo, Integer pageSize, Activity activity) {
        PageMethod.startPage(pageNo, pageSize);
        List<Activity> list = activityMapper.selectByConditionsJoinUser(activity);
        return new PageInfo<>(list);
    }

    @Transactional
    @Override
    public boolean deleteActivityById(String[] id) {
        boolean result = true;

        ActivityRemarkExample activityRemarkExample = new ActivityRemarkExample();
        ActivityRemarkExample.Criteria activityRemarkExampleCriteria = activityRemarkExample.createCriteria();
        activityRemarkExampleCriteria.andActivityIdIn(List.of(id));

        long arc = activityRemarkMapper.countByExample(activityRemarkExample);
        long ard = activityRemarkMapper.deleteByExample(activityRemarkExample);

        if (arc != ard) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        ActivityExample activityExample = new ActivityExample();
        ActivityExample.Criteria activityExampleCriteria = activityExample.createCriteria();
        activityExampleCriteria.andIdIn(List.of(id));

        int deleted = activityMapper.deleteByExample(activityExample);

        if (deleted != id.length) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public Activity getActivityById(String id) {
        return activityMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateActivity(Activity activity, String editBy) {
        activity.setEditTime(new Date());
        activity.setEditBy(editBy);
        return activityMapper.updateByPrimaryKeySelective(activity);
    }

    @Override
    public List<ActivityRemark> getActivityRemarkByActivityId(String id) {
        ActivityRemarkExample activityRemarkExample = new ActivityRemarkExample();
        ActivityRemarkExample.Criteria criteria = activityRemarkExample.createCriteria();
        criteria.andActivityIdEqualTo(id);
        activityRemarkExample.setOrderByClause("create_time desc");
        List<ActivityRemark> list = activityRemarkMapper.selectByExample(activityRemarkExample);

        for (ActivityRemark remark : list) {
            if ("1".equals(remark.getEditFlag())) {
                remark.setEditBy(
                        userMapper.selectByPrimaryKey(remark.getEditBy()).getName()
                );
            }
            remark.setCreateBy(
                    userMapper.selectByPrimaryKey(remark.getCreateBy()).getName()
            );
        }

        return list;
    }

    @Transactional
    @Override
    public boolean deleteActivityRemarkById(String id) {
        boolean result = true;
        int deleted = activityRemarkMapper.deleteByPrimaryKey(id);
        if (deleted != 1) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public int addActivityRemark(String createBy, ActivityRemark remark) {
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateTime(new Date());
        remark.setCreateBy(createBy);
        remark.setEditFlag("0");
        return activityRemarkMapper.insert(remark);
    }

    @Override
    public int updateActivityRemark(String editBy, ActivityRemark remark) {
        remark.setEditBy(editBy);
        remark.setEditTime(new Date());
        remark.setEditFlag("1");
        return activityRemarkMapper.updateByPrimaryKeySelective(remark);
    }

    @Override
    public List<Activity> getActivityByClueId(String id) {

        // 根据 Clue ID 从市场活动线索关系表中查出 Activity ID
        ClueActivityRelationExample clueActivityRelationExample = new ClueActivityRelationExample();
        ClueActivityRelationExample.Criteria clueActivityRelationExampleCriteria = clueActivityRelationExample.createCriteria();
        clueActivityRelationExampleCriteria.andClueIdEqualTo(id);
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectByExample(clueActivityRelationExample);

        List<String> activityIdList = new ArrayList<>();
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            activityIdList.add(clueActivityRelation.getActivityId());
        }

        if (activityIdList.isEmpty()) {
            return Collections.emptyList();
        }

        // 根据查出的 Activity ID 从市场活动表中查出 Activity
        ActivityExample activityExample = new ActivityExample();
        ActivityExample.Criteria activityExampleCriteria = activityExample.createCriteria();
        activityExampleCriteria.andIdIn(activityIdList);
        List<Activity> activityList = activityMapper.selectByExample(activityExample);

        // 将 Activity 中的 owner id 替换为 owner name
        for (Activity activity : activityList) {
            activity.setOwner(
                    userMapper.selectByPrimaryKey(activity.getOwner()).getName()
            );
            // 将 Activity 中的 Activity ID 替换为关系表中的 ID，方便后续作为解除关联参数
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                if (clueActivityRelation.getActivityId().equals(activity.getId())) {
                    activity.setId(clueActivityRelation.getId());
                }
            }
        }

        return activityList;
    }

    @Override
    public List<Activity> getActivityListExcludeBound(String clueId, String query) {

        // 先查出 clueId 目前关联的 activity ID
        ClueActivityRelationExample clueActivityRelationExample = new ClueActivityRelationExample();
        clueActivityRelationExample.createCriteria().andClueIdEqualTo(clueId);
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectByExample(clueActivityRelationExample);

        List<String> activityIdListToExclude = new ArrayList<>();
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            activityIdListToExclude.add(clueActivityRelation.getActivityId());
        }

        // 将以上查出来的 activity ID 排除，并模糊查找
        ActivityExample activityExample = new ActivityExample();
        ActivityExample.Criteria activityExampleCriteria = activityExample.createCriteria();
        if (!activityIdListToExclude.isEmpty()) {
            activityExampleCriteria.andIdNotIn(activityIdListToExclude);
        }
        activityExampleCriteria.andNameLike("%" + query + "%");
        List<Activity> activityList = activityMapper.selectByExample(activityExample);

        // 替换 owner
        for (Activity activity : activityList) {
            activity.setOwner(
                    userMapper.selectByPrimaryKey(activity.getOwner()).getName()
            );
        }

        return activityList;
    }

    @Override
    public List<Activity> getActivityList(String query) {
        ActivityExample activityExample = new ActivityExample();
        activityExample.createCriteria().andNameLike("%" + query + "%");
        List<Activity> activityList = activityMapper.selectByExample(activityExample);

        // 替换 owner
        for (Activity activity : activityList) {
            activity.setOwner(
                    userMapper.selectByPrimaryKey(activity.getOwner()).getName()
            );
        }

        return activityList;
    }
}
