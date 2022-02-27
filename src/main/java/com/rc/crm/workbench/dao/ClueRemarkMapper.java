package com.rc.crm.workbench.dao;

import com.rc.crm.workbench.domain.ClueRemark;
import com.rc.crm.workbench.domain.ClueRemarkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClueRemarkMapper {
    long countByExample(ClueRemarkExample example);

    int deleteByExample(ClueRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(ClueRemark record);

    int insertSelective(ClueRemark record);

    List<ClueRemark> selectByExample(ClueRemarkExample example);

    ClueRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ClueRemark record, @Param("example") ClueRemarkExample example);

    int updateByExample(@Param("record") ClueRemark record, @Param("example") ClueRemarkExample example);

    int updateByPrimaryKeySelective(ClueRemark record);

    int updateByPrimaryKey(ClueRemark record);
}