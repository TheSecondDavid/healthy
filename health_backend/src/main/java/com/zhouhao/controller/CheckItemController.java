package com.zhouhao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.PageResult;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.CheckItem;
import com.zhouhao.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {
    @Reference
    CheckItemService checkItemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        Integer add = checkItemService.add(checkItem);
        if(add == 1)
            return new Result(true, "插入成功");
        else
            return new Result(false, "插入失败");
    }

    @RequestMapping("/find")
    public Result find(){
        List<CheckItem> checkItems = checkItemService.find();
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItems);
    }

    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean){
        PageInfo<CheckItem> byPage = checkItemService.findByPage(queryPageBean);
        return new PageResult(byPage.getTotal(), byPage.getList());
    }

    @RequestMapping("/deleteById")
    public Result deleteById(String id){
        int i = checkItemService.deleteById(id);
        if(i == 0)
            return new Result(false, "删除失败");
        return new Result(true, "删除成功");
    }

    @RequestMapping("/findById")
    public Result findById(String id){
        CheckItem checkItem = checkItemService.findById(id);
        if(checkItem != null)
            return new Result(true, "查询成功", checkItem);
        return new Result(false, "接口出错");
    }

    @RequestMapping("/updateById")
    public Result updateById(@RequestBody CheckItem checkItem){
        int i = checkItemService.updateById(checkItem);
        if(i == 0)
            return new Result(false, "更新失败");
        return new Result(true, "更新成功");
    }
}
