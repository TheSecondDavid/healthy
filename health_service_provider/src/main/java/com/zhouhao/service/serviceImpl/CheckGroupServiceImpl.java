package com.zhouhao.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.dao.CheckGroupDao;
import com.zhouhao.entity.PageResult;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.CheckGroup;
import com.zhouhao.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional  // 这个注解不生效啊
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public Integer addCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        System.out.println(checkitemIds);
        Integer integer = checkGroupDao.addCheckGroup(checkGroup);
        int id = checkGroup.getId();
        for(int i = 0; i < checkitemIds.length; i++){
            checkGroupDao.setRelation(id, checkitemIds[i]);
        }
        return integer;
    }

    @Override
    public PageResult queryByPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        List<CheckGroup> checkGroups = checkGroupDao.query(queryPageBean.getQueryString());
        PageInfo<CheckGroup> pageInfo = new PageInfo<>(checkGroups);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Result getById(String id) {
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupDao.getById(id));
    }

    @Override
    public Result update(CheckGroup checkGroup, Integer[] checkitemIds) {
        Integer integer = checkGroupDao.update(checkGroup);
        Integer id = checkGroup.getId();
        checkGroupDao.deleteRelation(id);
        for(int i = 0; i < checkitemIds.length; i++){
            checkGroupDao.setRelation(id, checkitemIds[i]);
        }
        if(integer == 1)
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
    }

    @Override
    public Result queryAll() {
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupDao.queryAll());
    }
}