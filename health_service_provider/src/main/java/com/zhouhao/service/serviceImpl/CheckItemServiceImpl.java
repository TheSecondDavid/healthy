package com.zhouhao.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhouhao.dao.CheckItemDao;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.pojo.CheckItem;
import com.zhouhao.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional  // 这个注解不生效啊
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public Integer add(CheckItem checkItem) {
        return checkItemDao.add(checkItem);
    }

    @Override
    public PageInfo<CheckItem> findByPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<CheckItem> checkItems = checkItemDao.find(queryPageBean.getQueryString());
        return new PageInfo<>(checkItems);
    }

    @Override
    public int deleteById(String id) {
        try{
            return checkItemDao.deleteById(id);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public CheckItem findById(String id) {
        return checkItemDao.findById(id);
    }

    @Override
    public Integer updateById(CheckItem checkItem) {
        return checkItemDao.updateById(checkItem);
    }

    @Override
    public List<CheckItem> find() {
        return checkItemDao.findAll();
    }
}
