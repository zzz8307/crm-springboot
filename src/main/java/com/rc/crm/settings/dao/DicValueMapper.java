package com.rc.crm.settings.dao;

import com.rc.crm.settings.domain.DicValue;
import com.rc.crm.settings.domain.DicValueExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DicValueMapper {
    long countByExample(DicValueExample example);

    int deleteByExample(DicValueExample example);

    int deleteByPrimaryKey(String id);

    int insert(DicValue record);

    int insertSelective(DicValue record);

    List<DicValue> selectByExample(DicValueExample example);

    DicValue selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") DicValue record, @Param("example") DicValueExample example);

    int updateByExample(@Param("record") DicValue record, @Param("example") DicValueExample example);

    int updateByPrimaryKeySelective(DicValue record);

    int updateByPrimaryKey(DicValue record);
}