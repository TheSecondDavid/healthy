package com.zhouhao.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhouhao.dao.OrderDao;
import com.zhouhao.dao.OrderSettingDao;
import com.zhouhao.pojo.Member;
import com.zhouhao.pojo.Order;
import com.zhouhao.pojo.OrderSetting;
import com.zhouhao.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public Integer Order(Member member) {
        // 判断是不是会员，不是则创建
        Member vip = orderDao.find(member);
        if(vip == null){
            orderDao.order(member);
            return member.getId();
        }
        return vip.getId();
    }

    @Override
    public Integer realOrder(Order order) {
        Order repeat = orderDao.realfind(order);
        if(repeat != null){
            return -1;
        }

        orderDao.realOrder(order);

        // 修改预约记录表
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        OrderSetting orderSetting = orderSettingDao.getDayOrder(sdf.format(order.getOrderDate()));
        if(orderSetting != null)
            orderSettingDao.update2(sdf.format(order.getOrderDate()), String.valueOf(orderSetting.getNumber() - 1), String.valueOf(orderSetting.getReservations()+ 1));
        return 1;
    }

    @Override
    public Map<String, String> findById(String id) {
        return orderDao.findById(id);
    }

    @Override
    public Order findByYuyue(String orderDate, Integer memeberId) {
        return orderDao.findByYuyue(orderDate, memeberId);
    }

    @Override
    public List<Map> getOrderNumbers() {
        return orderDao.getOrderNumbers();
    }
}
