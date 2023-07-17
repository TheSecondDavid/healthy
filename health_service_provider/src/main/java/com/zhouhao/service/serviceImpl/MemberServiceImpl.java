package com.zhouhao.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhouhao.dao.MemberDao;
import com.zhouhao.pojo.Member;
import com.zhouhao.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
    @Override
    public List<Map> selectReport() {
        return memberDao.selectReport();
    }
}
