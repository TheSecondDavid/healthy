package com.zhouhao.dao;

import com.zhouhao.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

    void save(@Param("date") Date date,@Param("num") String num);

    List<OrderSetting> getOrder(@Param("year") String year, @Param("month") String month);

    int update(@Param("date") String date, @Param("num") String num);

    int update2(@Param("date") String date, @Param("num") String num, @Param("reservations") String reservations);

    void insert(@Param("date") String date, @Param("num") String num);

    OrderSetting getDayOrder(@Param("date") String date);
}
