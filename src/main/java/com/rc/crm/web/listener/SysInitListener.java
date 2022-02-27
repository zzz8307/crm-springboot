package com.rc.crm.web.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rc.crm.settings.domain.DicValue;
import com.rc.crm.settings.service.DicService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author rc
 */

@Component
public class SysInitListener {

    @Resource
    private DicService dicService;

    @Resource
    private ServletContext application;

    @EventListener(ContextRefreshedEvent.class)
    public void loadDataDictionary() {
        long begin = System.nanoTime();
        Map<String, List<DicValue>> dataDictionary = dicService.getDataDictionary();
        Set<String> keys = dataDictionary.keySet();
        for (String key : keys) {
            application.setAttribute(key, dataDictionary.get(key));
        }

        Map<String, String> possibilityMap = new HashMap<>(9);
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys1 = bundle.getKeys();
        while (keys1.hasMoreElements()) {
            String key = keys1.nextElement();
            String value = bundle.getString(key);
            possibilityMap.put(key, value);
        }
        application.setAttribute("possibilityMap", possibilityMap);
        // ObjectMapper mapper = new ObjectMapper();
        // String possibility = mapper.writeValueAsString(possibilityMap);
        // application.setAttribute("possibility", possibility);
        System.out.println("Data dictionary loaded in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - begin) + " ms.");
    }
}
