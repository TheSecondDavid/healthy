package com.zhouhao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.Result;
import com.zhouhao.service.MemberService;
import com.zhouhao.service.OrderService;
import com.zhouhao.service.ReportService;
import com.zhouhao.service.SetMealService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report")
@SuppressWarnings("all")
public class ReportController {
    @Reference
    MemberService memberService;
    @Reference
    SetMealService setMealService;
    @Reference
    OrderService orderService;
    @Reference
    ReportService reportService;

    @RequestMapping("getMemberReport")
    public Result getMemberReport() {
        List<Map> map = memberService.selectReport();
        Map map_new = new HashMap();
        ArrayList<Object> list1 = new ArrayList<>();
        ArrayList<Object> list2 = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            list1.add(new SimpleDateFormat("yyyy-MM-dd").format(map.get(i).get("regTime")));
            list2.add(map.get(i).get("count"));
        }
        map_new.put("months", list1);
        map_new.put("memberCount", list2);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map_new);
    }

    @RequestMapping("getSetmealReport")
    public Result getSetmealReport() {
        Map map = new HashMap();
        List<Map> maps = orderService.getOrderNumbers();
        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            list1.add(maps.get(i).get("name"));
            Map tempMap = new HashMap();
            tempMap.put("name", maps.get(i).get("name"));
            tempMap.put("value", maps.get(i).get("count(*)"));
            list2.add(tempMap);
        }
        map.put("setmealNames", list1);
        map.put("setmealCount", list2);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
    }

    @RequestMapping("getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> map = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
    }

    @RequestMapping("exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //远程调用报表服务获取报表数据
        Map<String, Object> result = reportService.getBusinessReportData();

        //取出返回结果数据，准备将报表数据写入到Excel文件中
        String reportDate = (String) result.get("reportDate");
        Integer todayNewMember = (Integer) result.get("todayNewMember");
        Integer totalMember = (Integer) result.get("totalMember");
        Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
        Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
        Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
        Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
        Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
        Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
        Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
        Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

        //获得Excel模板文件绝对路径
        String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
                File.separator + "report_template.xlsx";

        //读取模板文件创建Excel表格对象
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
        XSSFSheet sheet = workbook.getSheetAt(0);

        XSSFRow row = sheet.getRow(2);
        row.getCell(5).setCellValue(reportDate);//日期

        row = sheet.getRow(4);
        row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
        row.getCell(7).setCellValue(totalMember);//总会员数

        row = sheet.getRow(5);
        row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
        row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

        row = sheet.getRow(7);
        row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
        row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

        row = sheet.getRow(8);
        row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
        row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

        row = sheet.getRow(9);
        row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
        row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

        int rowNum = 12;
        for (Map map : hotSetmeal) {//热门套餐
            String name = (String) map.get("name");
            Long setmeal_count = (Long) map.get("setmeal_count");
            BigDecimal proportion = (BigDecimal) map.get("proportion");
            row = sheet.getRow(rowNum++);
            row.getCell(4).setCellValue(name);//套餐名称
            row.getCell(5).setCellValue(setmeal_count);//预约数量
            row.getCell(6).setCellValue(proportion.doubleValue());//占比
        }

        //通过输出流进行文件下载
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
        workbook.write(out);

        out.flush();
        out.close();
        workbook.close();

        return null;
    }

    @RequestMapping("genernatePdf")
    public void genernatePdf() throws JRException {
        String s = String.valueOf(this.getClass().getResource("/health_business.jrxml"));
        String jrxmlPath = s.substring(6, s.length());
        String jasperPath = s.substring(6, s.length()).replace("jrxml", "jasper");

        Map<String, Object> result = reportService.getBusinessReportData();
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, result, new JRBeanCollectionDataSource(hotSetmeal));
        String pdfPath = s.substring(6, s.length()).replace("jrxml", "pdf");
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
    }
}
