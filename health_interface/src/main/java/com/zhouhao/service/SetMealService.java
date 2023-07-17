package com.zhouhao.service;

import com.zhouhao.entity.PageResult;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.pojo.Setmeal;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

public interface SetMealService {
    Integer add(Integer[] checkgroupIds, Setmeal setmeal) throws TemplateException, IOException;

    PageResult query(QueryPageBean pageBean);

    List<Setmeal> getAll();

    Setmeal findById(String id);
}
