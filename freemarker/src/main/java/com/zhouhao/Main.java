package com.zhouhao;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("C:\\Users\\91192\\IdeaProjects\\health_parent\\freemarker\\src\\main\\resources\\"));
        configuration.setDefaultEncoding("utf-8");

        Template template = configuration.getTemplate("demo.ftl");
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("message", "欢迎来到传智播客");
        map.put("success", true);
        List<Map<String, Object>> goods=new ArrayList<>();

        Map<String, Object> good1=new HashMap<>();
        good1.put("name", "苹果");
        good1.put("price", 5.8);
        Map<String, Object> good2=new HashMap<>();
        good2.put("name", "香蕉");
        good2.put("price", 2.5);
        Map<String, Object> good3=new HashMap<>();
        good3.put("name", "橘子");
        good3.put("price", 3.2);
        goods.add(good1);
        goods.add(good2);
        goods.add(good3);
        map.put("goods", goods);

        FileWriter fileWriter = new FileWriter(new File("C:\\Users\\91192\\IdeaProjects\\health_parent\\freemarker\\src\\main\\resources\\demo.html"));
        template.process(map, fileWriter);
        fileWriter.close();
    }
}
