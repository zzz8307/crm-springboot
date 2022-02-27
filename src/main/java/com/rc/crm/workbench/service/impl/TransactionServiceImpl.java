package com.rc.crm.workbench.service.impl;

import com.rc.crm.settings.dao.UserMapper;
import com.rc.crm.util.DateTimeUtil;
import com.rc.crm.util.UUIDUtil;
import com.rc.crm.workbench.dao.*;
import com.rc.crm.workbench.domain.*;
import com.rc.crm.workbench.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author rc
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Resource
    private TranMapper tranMapper;
    @Resource
    private TranHistoryMapper tranHistoryMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private ContactsMapper contactsMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ActivityMapper activityMapper;

    @Override
    public List<Tran> getTransList() {
        List<Tran> tranList = tranMapper.selectByExample(new TranExample());
        for (Tran tran : tranList) {
            tran.setCustomerId(
                    customerMapper.selectByPrimaryKey(tran.getCustomerId()).getName()
            );
            tran.setOwner(
                    userMapper.selectByPrimaryKey(tran.getOwner()).getName()
            );
            tran.setContactsId(
                    contactsMapper.selectByPrimaryKey(tran.getContactsId()).getFullname()
            );
        }
        return tranList;
    }

    @Override
    public Tran getTran(String id) {
        Tran tran = tranMapper.selectByPrimaryKey(id);
        tran.setCustomerId(
                customerMapper.selectByPrimaryKey(tran.getCustomerId()).getName()
        );
        tran.setOwner(
                userMapper.selectByPrimaryKey(tran.getOwner()).getName()
        );
        tran.setContactsId(
                contactsMapper.selectByPrimaryKey(tran.getContactsId()).getFullname()
        );
        tran.setActivityId(
                activityMapper.selectByPrimaryKey(tran.getActivityId()).getName()
        );
        tran.setCreateBy(
                userMapper.selectByPrimaryKey(tran.getCreateBy()).getName()
        );

        String editBy = tran.getEditBy();
        if (editBy != null && !editBy.isEmpty()) {
            tran.setEditBy(
                    userMapper.selectByPrimaryKey(editBy).getName()
            );
        }
        return tran;
    }

    @Override
    public boolean addTransaction(Tran tran, String customerName, String createBy) {
        boolean result = true;
        String createTime = DateTimeUtil.getSysTime();

        // 提交表单客户上传名字，后台处理，最终保存的是customerId
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andNameEqualTo(customerName);
        List<Customer> customerList = customerMapper.selectByExample(customerExample);
        Customer customer = null;

        // 查询是否有此客户，如果没有则添加客户记录
        if (customerList.isEmpty()) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);

            int customerInserted = customerMapper.insert(customer);
            if (customerInserted != 1) {
                result = false;
            }
        } else {
            customer = customerList.get(0);
        }

		// 添加交易记录（注意先后顺序，添加完客户之后，才知道客户id）
        tran.setId(UUIDUtil.getUUID());
        tran.setCustomerId(customer.getId());
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);

        int tranInserted = tranMapper.insert(tran);
        if (tranInserted != 1) {
            result = false;
        }

        // 添加交易历史
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

        if (!result) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public List<TranHistory> getTranHistoryList(String id) {
        TranHistoryExample tranHistoryExample = new TranHistoryExample();
        tranHistoryExample.createCriteria().andTranIdEqualTo(id);
        tranHistoryExample.setOrderByClause("create_time desc");
        List<TranHistory> tranHistoryList = tranHistoryMapper.selectByExample(tranHistoryExample);
        for (TranHistory tranHistory : tranHistoryList) {
            tranHistory.setCreateBy(
                    userMapper.selectByPrimaryKey(tranHistory.getCreateBy()).getName()
            );
        }
        return tranHistoryList;
    }

    @Transactional
    @Override
    public boolean changeStage(String id, String stage, String editBy) {
        boolean result = true;
        String editTime = DateTimeUtil.getSysTime();

        Tran tran = tranMapper.selectByPrimaryKey(id);
        tran.setStage(stage);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);

        int tranInserted = tranMapper.updateByPrimaryKey(tran);
        if (tranInserted != 1) {
            result = false;
        }

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(stage);
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setTranId(id);
        tranHistory.setCreateBy(editBy);
        tranHistory.setCreateTime(editTime);

        int tranHistoryInserted = tranHistoryMapper.insert(tranHistory);
        if (tranHistoryInserted != 1) {
            result = false;
        }

        if (!result) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public long countAll() {
        return tranMapper.countByExample(new TranExample());
    }

    @Override
    public List<Map<String, Object>> getListCountByStage() {
        return tranMapper.countByStage();
    }
}
