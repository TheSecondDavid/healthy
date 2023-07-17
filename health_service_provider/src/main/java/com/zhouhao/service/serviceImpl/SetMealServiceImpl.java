package com.zhouhao.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhouhao.constant.RedisConstant;
import com.zhouhao.dao.SetMealDao;
import com.zhouhao.entity.PageResult;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.pojo.Setmeal;
import com.zhouhao.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    SetMealDao setMealDao;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    String outputPath;

    @Override
    public Integer add(Integer[] checkgroupIds, Setmeal setmeal) throws TemplateException, IOException {
        setMealDao.add(setmeal);
        Integer setmealId = setmeal.getId();

        try{
        if(checkgroupIds != null)
            for(Integer checkgroupId: checkgroupIds){
                setMealDao.setRelations(setmealId, checkgroupId);
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

        Jedis jedis = jedisPool.getResource();
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());

        generateHtml();
        return 1;
    }

    @Override
    public PageResult query(QueryPageBean pageBean) {
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        List<Setmeal> setmealList = setMealDao.query(pageBean.getQueryString());
        PageInfo pageInfo = new PageInfo(setmealList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<Setmeal> getAll() {
        return setMealDao.getAll();
    }

    @Override
    public Setmeal findById(String id) {
        return setMealDao.findById(id);
    }

    public void generateHtml() throws TemplateException, IOException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        generateMobileSetmealListHtml(configuration);
        generateMobileSetmealDetailHtml(configuration);
    }

    public void generateMobileSetmealListHtml(Configuration configuration) throws IOException, TemplateException {
        Template template = configuration.getTemplate("mobile_setMeal.ftl");

        Map<String, Object> map = new HashMap<>();
        List<Setmeal> all = getAll();
        map.put("setmealList", all);

        FileWriter writer = new FileWriter(outputPath + "mySetMeal.html");
        template.process(map, writer);
    }

    public void generateMobileSetmealDetailHtml(Configuration configuration) throws IOException, TemplateException {
        Template template = configuration.getTemplate("mobile_setMeal_detail.ftl");
        List<Setmeal> all = getAll();
        for(Setmeal setmeal: all){
            Map<String, Setmeal> map = new HashMap<>();
            Setmeal byId = findById(String.valueOf(setmeal.getId()));
            FileWriter writer = new FileWriter(outputPath + "setMeal_detail_" + setmeal.getId() + ".html");
            map.put("setMeal", byId);
            template.process(map ,writer);
        }
    }
}
