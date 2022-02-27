package com.rc.crm.vo;

import java.util.List;

/**
 * @author rc
 */
public class PaginationVO<T> {
    private Long total;
    private List<T> dataList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
