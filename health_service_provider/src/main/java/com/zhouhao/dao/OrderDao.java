package com.zhouhao.dao;

import com.zhouhao.pojo.Member;
import com.zhouhao.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
@SuppressWarnings("all")
public interface OrderDao {
    int order(Member member);
    Member find(Member member);

    Order realfind(Order order);
    void realOrder(Order order);

    Map<String, String> findById(String id);

    Order findByYuyue(@Param("orderDate") String orderDate,@Param("memberId") Integer memberId);

    List<Map> getOrderNumbers();
}
