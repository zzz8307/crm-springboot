package com.rc.crm.workbench.service.impl;

import com.rc.crm.workbench.dao.CustomerMapper;
import com.rc.crm.workbench.domain.Customer;
import com.rc.crm.workbench.domain.CustomerExample;
import com.rc.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rc
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public List<String> getCustomerName(String name) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andNameLike("%" + name + "%");
        List<Customer> customerList = customerMapper.selectByExample(customerExample);
        List<String> nameList = new ArrayList<>();
        for (Customer customer : customerList) {
            nameList.add(customer.getName());
        }
        return nameList;
    }
}
