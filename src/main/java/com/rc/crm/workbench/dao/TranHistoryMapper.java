package com.rc.crm.workbench.dao;

import com.rc.crm.workbench.domain.TranHistory;
import com.rc.crm.workbench.domain.TranHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TranHistoryMapper {
    long countByExample(TranHistoryExample example);

    int deleteByExample(TranHistoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(TranHistory record);

    int insertSelective(TranHistory record);

    List<TranHistory> selectByExample(TranHistoryExample example);

    TranHistory selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TranHistory record, @Param("example") TranHistoryExample example);

    int updateByExample(@Param("record") TranHistory record, @Param("example") TranHistoryExample example);

    int updateByPrimaryKeySelective(TranHistory record);

    int updateByPrimaryKey(TranHistory record);
}