package com.zhouhao.service;

import com.zhouhao.entity.Result;
import java.util.Date;

public interface OrderSettingService {

    void save(Date date, String num);
    Result getOrder(String year, String month);
    Result update(String date, String num);

    Integer getOrderByDay(Date orderDate);
}
