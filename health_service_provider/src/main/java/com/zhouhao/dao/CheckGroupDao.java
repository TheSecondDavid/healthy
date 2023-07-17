package com.zhouhao.dao;

import com.zhouhao.pojo.CheckGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CheckGroupDao {
    Integer addCheckGroup(CheckGroup checkGroup);

    void setRelation(@Param("id") Integer id, @Param("checkitemId") Integer checkitemId);

    List<CheckGroup> query(@Param("queryString") String queryString);

    Object getById(@Param("id") String id);

    Integer update(CheckGroup checkGroup);

    void deleteRelation(@Param("id") Integer id);

    List<CheckGroup> queryAll();
}
