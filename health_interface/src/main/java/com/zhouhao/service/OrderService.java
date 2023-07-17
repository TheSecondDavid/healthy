package com.zhouhao.service;

import com.zhouhao.pojo.Member;
import com.zhouhao.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Integer Order(Member member);

    Integer realOrder(Order order);

    Map findById(String id);

    Order findByYuyue(String orderDate, Integer memberId);

    List<Map> getOrderNumbers();
}
