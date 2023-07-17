package com.zhouhao.dao;

import com.zhouhao.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CheckItemDao {
    Integer add(CheckItem checkItem);

    List<CheckItem> find(@Param("queryString") String queryString);

    int deleteById(String id);

    CheckItem findById(String id);

    Integer updateById(CheckItem checkItem);

    List<CheckItem> findAll();
}
