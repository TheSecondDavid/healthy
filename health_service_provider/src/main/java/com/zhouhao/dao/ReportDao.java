package com.zhouhao.dao;

import org.apache.ibatis.annotations.MapKey;

import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public interface ReportDao {
    Integer todayNewMember(Date date);
    Integer totalMember(Date date);
    Integer thisWeekNewMember(Date date);
    Integer thisMonthNewMember(Date date);
    Integer todayOrderNumber(Date date);
    Integer todayVisitsNumber(Date date);
    Integer thisWeekOrderNumber(Date date);
    Integer thisWeekVisitsNumber(Date date);
    Integer thisMonthOrderNumber(Date date);
    Integer thisMonthVisitsNumber(Date date);
    @MapKey("null")
    List<Map> hotSetmeals();
}
