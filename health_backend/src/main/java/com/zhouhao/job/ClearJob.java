package com.zhouhao.job;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.zhouhao.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.Set;

public class ClearJob {
    @Autowired
    JedisPool jedisPool;

    public void clearImg() {
        Jedis jedis = jedisPool.getResource();
        Set<String> set = jedis.smembers(RedisConstant.SETMEAL_PIC_RESOURCES);
        Set<String> needSet = jedis.smembers(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        set.removeAll(needSet);

        for (String key : set) {
            Configuration cfg = new Configuration(Region.autoRegion());
            String bucket = "healthy-zhouhao2";

            Auth auth = Auth.create("se2dlnryENNICOJB08Fu-xp7hg3mXu4dwRd4pIiZ", "z_s2cG7ThAhpzpsi3xJKhGh1rKzBAzQCMps5GVRU");
            BucketManager bucketManager = new BucketManager(auth, cfg);

            try {
                bucketManager.delete(bucket, key);
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES, key);
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
    }
}
