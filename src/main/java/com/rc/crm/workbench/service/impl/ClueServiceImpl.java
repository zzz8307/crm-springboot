package com.rc.crm.workbench.service.impl;

import com.rc.crm.settings.dao.UserMapper;
import com.rc.crm.util.DateTimeUtil;
import com.rc.crm.util.UUIDUtil;
import com.rc.crm.workbench.dao.*;
import com.rc.crm.workbench.domain.*;
import com.rc.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author rc
 */
@Service
public class ClueServiceImpl implements ClueService {

    @Resource
    private ClueMapper clueMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private ContactsMapper contactsMapper;
    @Resource
    private ClueRemarkMapper clueRemarkMapper;
    @Resource
    private CustomerRemarkMapper customerRemarkMapper;
    @Resource
    private ContactsRemarkMapper contactsRemarkMapper;
    @Resource
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Resource
    private TranMapper tranMapper;
    @Resource
    private TranHistoryMapper tranHistoryMapper;
    @Resource
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public int addClue(Clue clue, String createBy) {
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateBy(createBy);
        clue.setCreateTime(DateTimeUtil.getSysTime());
        return clueMapper.insert(clue);
    }

    @Override
    public Clue getClueById(String id) {
        Clue clue = clueMapper.selectByPrimaryKey(id);
        clue.setOwner(
                userMapper.selectByPrimaryKey(clue.getOwner()).getName()
        );

        clue.setCreateBy(
                userMapper.selectByPrimaryKey(clue.getCreateBy()).getName()
        );

        String editBy = clue.getEditBy();
        if (editBy != null && !"".equals(editBy)) {
            clue.setEditBy(
                    userMapper.selectByPrimaryKey(editBy).getName()
            );
        }
        return clue;
    }

    @Override
    public List<Clue> getClues() {
        List<Clue> clueList = clueMapper.selectByExample(new ClueExample());
        for (Clue clue : clueList) {
            clue.setOwner(
                    userMapper.selectByPrimaryKey(clue.getOwner()).getName()
            );
        }
        return clueList;
    }

    @Transactional
    @Override
    public boolean deleteClueById(String id) {
        boolean result = true;
        int deleted = clueActivityRelationMapper.deleteByPrimaryKey(id);
        if (deleted != 1) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Transactional
    @Override
    public boolean bindActivity(String[] activityIds, String clueId) {
        boolean result = true;
        int inserted = 0;
        for (String id : activityIds) {
            ClueActivityRelation relation = new ClueActivityRelation();
            relation.setId(UUIDUtil.getUUID());
            relation.setClueId(clueId);
            relation.setActivityId(id);
            inserted += clueActivityRelationMapper.insert(relation);
        }

        if (inserted != activityIds.length) {
            result = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Transactional
    @Override
    public boolean convert(String clueId, Boolean isCreateTransaction, Tran tran, String createBy) {
        boolean result = true;
        String createTime = DateTimeUtil.getSysTime();

        // (1) ???????????????id???????????????id??????????????????????????????????????????????????????????????????
        Clue clue = clueMapper.selectByPrimaryKey(clueId);

        // (2) ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        String companyName = clue.getCompany();
        Customer customer = null;
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andNameEqualTo(companyName);
        List<Customer> customerList = customerMapper.selectByExample(customerExample);
        if (customerList.isEmpty()) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(companyName);
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());

            int customerInserted = customerMapper.insert(customer);
            if (customerInserted != 1) {
                result = false;
            }
        } else {
            customer = customerList.get(0);
        }

        // (3) ?????????????????????????????????????????????????????????
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());

        int contactsInserted = contactsMapper.insert(contacts);
        if (contactsInserted != 1) {
            result = false;
        }

        // (4) ??????????????????????????????????????????????????????
        ClueRemarkExample clueRemarkExample = new ClueRemarkExample();
        clueRemarkExample.createCriteria().andClueIdEqualTo(clueId);
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectByExample(clueRemarkExample);
        if (!clueRemarkList.isEmpty()) {
            for (ClueRemark clueRemark : clueRemarkList) {
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(createTime);
                customerRemark.setCustomerId(customer.getId());

                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(createTime);
                contactsRemark.setContactsId(contacts.getId());

                int customerRemarkInserted = customerRemarkMapper.insert(customerRemark);
                int contactsRemarkInserted = contactsRemarkMapper.insert(contactsRemark);
                if (customerRemarkInserted != 1 || contactsRemarkInserted != 1) {
                    result = false;
                }
            }
        }

        // (5) ????????????????????????????????????????????????????????????????????????????????????
        ClueActivityRelationExample clueActivityRelationExample = new ClueActivityRelationExample();
        clueActivityRelationExample.createCriteria().andClueIdEqualTo(clueId);
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectByExample(clueActivityRelationExample);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());

            int contactsActivityRelationInserted = contactsActivityRelationMapper.insert(contactsActivityRelation);
            if (contactsActivityRelationInserted != 1) {
                result = false;
            }
        }

        // (6) ????????????????????????????????????????????????
        if (Boolean.TRUE.equals(isCreateTransaction)) {
            tran.setId(UUIDUtil.getUUID());
            tran.setOwner(clue.getOwner());
            tran.setCustomerId(customer.getId());
            tran.setSource(clue.getSource());
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());

            int tranInserted = tranMapper.insert(tran);
            if (tranInserted != 1) {
                result = false;
            }

            // (7) ??????????????????????????????????????????????????????????????????
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(tran.getId());

            int tranHistoryInserted = tranHistoryMapper.insert(tranHistory);
            if (tranHistoryInserted != 1) {
                result = false;
            }
        }

        // (8) ??????????????????
        int clueRemarkDeleted = clueRemarkMapper.deleteByExample(clueRemarkExample);
        if (clueRemarkDeleted != clueRemarkList.size()) {
            result = false;
        }

        // (9) ????????????????????????????????????
        int clueActivityRelationDeleted = clueActivityRelationMapper.deleteByExample(clueActivityRelationExample);
        if (clueActivityRelationDeleted != clueActivityRelationList.size()) {
            result = false;
        }

        // (10) ????????????
        int clueDeleted = clueMapper.deleteByPrimaryKey(clueId);
        if (clueDeleted != 1) {
            result = false;
        }

        if (!result) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }
}
