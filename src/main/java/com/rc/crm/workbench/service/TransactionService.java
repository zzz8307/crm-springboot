package com.rc.crm.workbench.service;

import com.rc.crm.workbench.domain.Tran;
import com.rc.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * @author rc
 */
public interface TransactionService {
    List<Tran> getTransList();

    boolean addTransaction(Tran tran, String customerName, String createBy);

    Tran getTran(String id);

    List<TranHistory> getTranHistoryList(String id);

    boolean changeStage(String id, String stage, String editBy);

    long countAll();

    List<Map<String, Object>> getListCountByStage();
}
