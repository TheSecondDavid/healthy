package com.zhouhao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.Result;
import com.zhouhao.service.OrderSettingService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Date;
import java.util.Map;

@RequestMapping("/orderSetting")
@RestController
public class OrderSettingController {
    static Logger logger = LogManager.getLogger(OrderSettingController.class);

    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("getOrder")
    public Result getOrder(@RequestParam String year,@RequestParam String month){
        return orderSettingService.getOrder(year, month);
    }

    @RequestMapping("upload")
    public Result upload(MultipartFile file){
        File myFile = null;
        try{
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String[] split = originalFilename.split("\\.");
            myFile = File.createTempFile(split[0], "." + split[1]);
            file.transferTo(myFile);
            Workbook workbook = new XSSFWorkbook(myFile);

            myFile.deleteOnExit();

            Sheet sheet = workbook.getSheetAt(0);
            int length = sheet.getLastRowNum();
            for(int i = 1; i < length + 1; i++){
                Row row = sheet.getRow(i);
                if(row.getCell(1) == null)
                    break;
                Date date = DateUtil.getJavaDate(row.getCell(0).getNumericCellValue());
                double num = row.getCell(1).getNumericCellValue();
                orderSettingService.save(date, String.valueOf(num));
            }

            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("你传错文件了！");
        }

        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    @RequestMapping("update")
    public Result update(@RequestBody Map<String, String> map){
        String date = map.get("date");
        String num = map.get("num");

        return orderSettingService.update(date, num);
    }
}
