package com.zhouhao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zhouhao.constant.MessageConstant;
import com.zhouhao.constant.RedisConstant;
import com.zhouhao.entity.PageResult;
import com.zhouhao.entity.QueryPageBean;
import com.zhouhao.entity.Result;
import com.zhouhao.pojo.Setmeal;
import com.zhouhao.service.SetMealService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {
    @Reference
    SetMealService setMealService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("query")
    @PreAuthorize("isAuthenticated()")
    public PageResult query(@RequestBody QueryPageBean pageBean){
        return setMealService.query(pageBean);
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")
    public Result add(Integer[] checkgroupIds, @RequestBody Setmeal setmeal) throws TemplateException, IOException {
        Integer in = setMealService.add(checkgroupIds, setmeal);
        if(in == 1)
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) throws QiniuException {
        String fileName = imgFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        DefaultPutRet putRet = null;
        try {
            File file = File.createTempFile(fileName, suffix);
            imgFile.transferTo(file);
            Configuration cfg = new Configuration(Region.autoRegion());
            UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
            String accessKey = "fUHuuJI2Ez4eSwxUEoE1jY6qfNvEOFGS7IdfV7xC";
            String secretKey = "AJ-KmtpLfHckCPhrO3-_dXdweASKZprLUGND59Ak";
            String bucket = "healthy-zhouhao3";
//默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = null;
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(file, key, upToken, null, null, false);
            //解析上传成功的结果
            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            ex.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Jedis jedis = jedisPool.getResource();
        jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES, putRet.key);
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, putRet.key);
    }
}
