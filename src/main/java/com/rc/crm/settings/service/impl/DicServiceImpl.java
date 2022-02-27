package com.rc.crm.settings.service.impl;

import com.rc.crm.settings.dao.DicTypeMapper;
import com.rc.crm.settings.dao.DicValueMapper;
import com.rc.crm.settings.domain.DicType;
import com.rc.crm.settings.domain.DicTypeExample;
import com.rc.crm.settings.domain.DicValue;
import com.rc.crm.settings.domain.DicValueExample;
import com.rc.crm.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rc
 */
@Service
public class DicServiceImpl implements DicService {

    @Resource
    private DicTypeMapper dicTypeMapper;

    @Resource
    private DicValueMapper dicValueMapper;

    @Override
    public Map<String, List<DicValue>> getDataDictionary() {
        List<DicType> dicTypeList = dicTypeMapper.selectByExample(new DicTypeExample());
        Map<String, List<DicValue>> dic = new HashMap<>(7);

        for (DicType dicType : dicTypeList) {
            String code = dicType.getCode();

            DicValueExample example = new DicValueExample();
            DicValueExample.Criteria criteria = example.createCriteria();
            example.setOrderByClause("order_no");
            criteria.andTypeCodeEqualTo(code);
            List<DicValue> dicValueList = dicValueMapper.selectByExample(example);

            dic.put(code + "List", dicValueList);
        }

        return dic;
    }
}
