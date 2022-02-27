package com.rc.crm.workbench.dao;

import com.rc.crm.workbench.domain.TranRemark;
import com.rc.crm.workbench.domain.TranRemarkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TranRemarkMapper {
    long countByExample(TranRemarkExample example);

    int deleteByExample(TranRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(TranRemark record);

    int insertSelective(TranRemark record);

    List<TranRemark> selectByExample(TranRemarkExample example);

    TranRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TranRemark record, @Param("example") TranRemarkExample example);

    int updateByExample(@Param("record") TranRemark record, @Param("example") TranRemarkExample example);

    int updateByPrimaryKeySelective(TranRemark record);

    int updateByPrimaryKey(TranRemark record);
}