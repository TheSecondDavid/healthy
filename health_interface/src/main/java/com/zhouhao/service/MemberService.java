package com.zhouhao.service;

import com.zhouhao.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Map> selectReport();
}
