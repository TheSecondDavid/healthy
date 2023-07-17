package com.zhouhao.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhouhao.dao.ReportDao;
import com.zhouhao.pojo.Setmeal;
import com.zhouhao.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    ReportDao reportDao;
    @Override
    public Map getBusinessReportData() {
        Integer members0 = reportDao.todayNewMember(new Date());
        Integer members1 = reportDao.totalMember(new Date());
        Integer members2 = reportDao.thisWeekNewMember(new Date());
        Integer members3 = reportDao.thisMonthNewMember(new Date());
        Integer members4 = reportDao.todayOrderNumber(new Date());
        Integer members5 = reportDao.todayVisitsNumber(new Date());
        Integer members6 = reportDao.thisWeekOrderNumber(new Date());
        Integer members7 = reportDao.thisWeekVisitsNumber(new Date());
        Integer members8 = reportDao.thisMonthOrderNumber(new Date());
        Integer members9 = reportDao.thisMonthVisitsNumber(new Date());
        List<Map> setmeals = reportDao.hotSetmeals();

        Map<String,Object> result = new HashMap<>();
        result.put("reportDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        result.put("todayNewMember",members0);
        result.put("totalMember",members1);
        result.put("thisWeekNewMember",members2);
        result.put("thisMonthNewMember",members3);
        result.put("todayOrderNumber",members4);
        result.put("thisWeekOrderNumber",members5);
        result.put("thisMonthOrderNumber",members6);
        result.put("todayVisitsNumber",members7);
        result.put("thisWeekVisitsNumber",members8);
        result.put("thisMonthVisitsNumber",members9);
        result.put("hotSetmeal",setmeals);

        return result;
    }
}
