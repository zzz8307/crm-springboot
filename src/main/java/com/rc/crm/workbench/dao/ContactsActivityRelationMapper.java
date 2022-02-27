package com.rc.crm.workbench.dao;

import com.rc.crm.workbench.domain.ContactsActivityRelation;
import com.rc.crm.workbench.domain.ContactsActivityRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContactsActivityRelationMapper {
    long countByExample(ContactsActivityRelationExample example);

    int deleteByExample(ContactsActivityRelationExample example);

    int deleteByPrimaryKey(String id);

    int insert(ContactsActivityRelation record);

    int insertSelective(ContactsActivityRelation record);

    List<ContactsActivityRelation> selectByExample(ContactsActivityRelationExample example);

    ContactsActivityRelation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ContactsActivityRelation record, @Param("example") ContactsActivityRelationExample example);

    int updateByExample(@Param("record") ContactsActivityRelation record, @Param("example") ContactsActivityRelationExample example);

    int updateByPrimaryKeySelective(ContactsActivityRelation record);

    int updateByPrimaryKey(ContactsActivityRelation record);
}