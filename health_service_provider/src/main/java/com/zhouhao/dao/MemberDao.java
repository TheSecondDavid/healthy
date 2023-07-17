package com.zhouhao.dao;

import com.zhouhao.pojo.Member;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(@Param("member") Member member);

    @MapKey("regTime")
    List<Map> selectReport();
}
