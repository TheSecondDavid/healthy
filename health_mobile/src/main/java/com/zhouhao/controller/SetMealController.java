package com.zhouhao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.Setmeal;
import com.zhouhao.service.SetMealService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {
    @Reference
    SetMealService setMealService;

    @RequestMapping("/getAll")
    public Result getAll(){
        List<Setmeal> setmeals = setMealService.getAll();
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeals);
    }

    @RequestMapping("/findById")
    public Result findById(@RequestBody String id){
        Setmeal setmeal = setMealService.findById(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }
}
