package com.zhouhao.service;

import com.zhouhao.entity.PageResult;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.CheckGroup;

public interface CheckGroupService {
    Integer addCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult queryByPage(QueryPageBean queryPageBean);

    Result getById(String id);

    Result update(CheckGroup checkGroup, Integer[] checkitemIds);

    Result queryAll();
}
