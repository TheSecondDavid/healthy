package com.zhouhao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.PageResult;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.CheckGroup;
import com.zhouhao.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        Integer integer = checkGroupService.addCheckGroup(checkGroup, checkitemIds);
        if(integer == 0){
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
    @RequestMapping("/queryByPage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PageResult queryByPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.queryByPage(queryPageBean);
        return pageResult;
    }
    @RequestMapping("/getById")
    public Result getById(String id){
        return checkGroupService.getById(id);
    }
    @RequestMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        return checkGroupService.update(checkGroup, checkitemIds);
    }
    @RequestMapping("queryAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result queryAll(){
        return checkGroupService.queryAll();
    }
}
