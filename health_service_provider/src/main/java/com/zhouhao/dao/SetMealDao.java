package com.zhouhao.dao;

import com.zhouhao.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SetMealDao {
    Integer add(Setmeal setmeal);
    Integer setRelations(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);
    List<Setmeal> query(@Param("queryString") String queryString);
    List<Setmeal> getAll();
    Setmeal findById(String id);
}
