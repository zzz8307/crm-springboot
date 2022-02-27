package com.rc.crm.settings.service;

import com.rc.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @author rc
 */
public interface DicService {
    Map<String, List<DicValue>> getDataDictionary();
}
