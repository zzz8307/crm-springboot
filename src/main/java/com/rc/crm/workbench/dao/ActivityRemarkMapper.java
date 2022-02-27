package com.rc.crm.workbench.dao;

import com.rc.crm.workbench.domain.ActivityRemark;
import com.rc.crm.workbench.domain.ActivityRemarkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActivityRemarkMapper {
    long countByExample(ActivityRemarkExample example);

    int deleteByExample(ActivityRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(ActivityRemark record);

    int insertSelective(ActivityRemark record);

    List<ActivityRemark> selectByExample(ActivityRemarkExample example);

    ActivityRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ActivityRemark record, @Param("example") ActivityRemarkExample example);

    int updateByExample(@Param("record") ActivityRemark record, @Param("example") ActivityRemarkExample example);

    int updateByPrimaryKeySelective(ActivityRemark record);

    int updateByPrimaryKey(ActivityRemark record);
}