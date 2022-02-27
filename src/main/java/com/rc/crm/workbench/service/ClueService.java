package com.rc.crm.workbench.service;

import com.rc.crm.workbench.domain.Clue;
import com.rc.crm.workbench.domain.Tran;

import java.util.List;

/**
 * @author rc
 */
public interface ClueService {
    int addClue(Clue clue, String createBy);

    Clue getClueById(String id);

    List<Clue> getClues();

    boolean deleteClueById(String id);

    boolean bindActivity(String[] activityIds, String clueId);

    boolean convert(String clueId, Boolean isCreateTransaction, Tran tran, String createBy);
}
