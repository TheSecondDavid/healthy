package com.zhouhao.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.dao.OrderSettingDao;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.OrderSetting;
import com.zhouhao.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void save(Date date, String num) {
        int update = orderSettingDao.update(new SimpleDateFormat("yyyy-MM-dd").format(date), num);
        if(update == 0){
            orderSettingDao.save(date, num);
        }
    }

    @Override
    public Result getOrder(String year, String month) {
        List<OrderSetting> orderSettings = orderSettingDao.getOrder(year, month);
        List<Map<String, Integer>> maps = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (OrderSetting orderSetting : orderSettings) {
            Map<String, Integer> map = new HashMap<>();
            map.put("date", orderSetting.getOrderDate().getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            maps.add(map);
        }

        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, maps);
    }

    @Override
    public Result update(String date, String num) {
        int update = orderSettingDao.update(date, num);
        if(update == 0){
            orderSettingDao.insert(date, num);
        }
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }

    @Override
    public Integer getOrderByDay(Date orderDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        OrderSetting orderSetting = orderSettingDao.getDayOrder(simpleDateFormat.format(orderDate));
        if(orderSetting == null)
            return -1;
        return orderSetting.getNumber();
    }
}

