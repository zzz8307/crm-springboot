package com.rc.crm.workbench.dao;

import com.rc.crm.workbench.domain.Tran;
import com.rc.crm.workbench.domain.TranExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TranMapper {
    long countByExample(TranExample example);

    int deleteByExample(TranExample example);

    int deleteByPrimaryKey(String id);

    int insert(Tran record);

    int insertSelective(Tran record);

    List<Tran> selectByExample(TranExample example);

    Tran selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Tran record, @Param("example") TranExample example);

    int updateByExample(@Param("record") Tran record, @Param("example") TranExample example);

    int updateByPrimaryKeySelective(Tran record);

    int updateByPrimaryKey(Tran record);

    List<Map<String, Object>> countByStage();
}