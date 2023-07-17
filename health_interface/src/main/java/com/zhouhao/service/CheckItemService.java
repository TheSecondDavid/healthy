package com.zhouhao.service;

import com.github.pagehelper.PageInfo;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.pojo.CheckItem;
import java.util.List;

public interface CheckItemService {
    Integer add(CheckItem checkItem);

    PageInfo<CheckItem> findByPage(QueryPageBean queryPageBean);

    int deleteById(String id);

    CheckItem findById(String id);

    Integer updateById(CheckItem checkItem);

    List<CheckItem> find();
}
