package com.rc.crm.workbench.dao;

import com.rc.crm.workbench.domain.ClueActivityRelation;
import com.rc.crm.workbench.domain.ClueActivityRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClueActivityRelationMapper {
    long countByExample(ClueActivityRelationExample example);

    int deleteByExample(ClueActivityRelationExample example);

    int deleteByPrimaryKey(String id);

    int insert(ClueActivityRelation record);

    int insertSelective(ClueActivityRelation record);

    List<ClueActivityRelation> selectByExample(ClueActivityRelationExample example);

    ClueActivityRelation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ClueActivityRelation record, @Param("example") ClueActivityRelationExample example);

    int updateByExample(@Param("record") ClueActivityRelation record, @Param("example") ClueActivityRelationExample example);

    int updateByPrimaryKeySelective(ClueActivityRelation record);

    int updateByPrimaryKey(ClueActivityRelation record);
}