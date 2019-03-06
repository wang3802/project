package com.bdqn.text;

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

public class TextDemo1 {
    public static void main(String[] args) throws Exception {
        TextDemo1.Text2();
    }
    //根据list 实体类，生成txt 文件
    public static void Text2() throws Exception {
         Animal en=new Animal("小狗",56);
         Animal cc=new Animal("小猫",57);

         List<Animal> animals=new ArrayList<>();
        animals.add(en);
        animals.add(cc);

        Map<String,Object> map=new HashMap<>();
        map.put("l",animals);

        Configuration configuration=new Configuration();
        configuration.setDefaultEncoding("utf-8");
        //定义模板路径
        configuration.setDirectoryForTemplateLoading(new File("F:\\学生 实例\\325\\Itrip\\itripauth\\src\\main\\resources"));
        //定义模板文件
        Template template=configuration.getTemplate("TextFrameMarkList.flt");


        template.process(map,new FileWriter("D://TT.txt"));
    }

    //测试单个字符串生成html
    public  void Text1() throws Exception {
        Map<String,Object> map=new HashMap<>();
        map.put("v1","V1的值");
        Configuration configuration=new Configuration();
        configuration.setDefaultEncoding("utf-8");
        //定义模板路径
        configuration.setDirectoryForTemplateLoading(new File("F:\\学生 实例\\325\\Itrip\\itripauth\\src\\main\\resources"));
        //定义模板文件
        Template template=configuration.getTemplate("TextFrameMark.flt");


        template.process(map,new FileWriter("D://NEW.html"));
    }
}
