package com.sibro.lucence;

import com.sibro.lucence.thread.CreateIndexTestThread;
import com.sibro.lucence.thread.SearchIndexTestThread;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
@MapperScan(basePackages = {"com.sibro.lucence.mapper"})
public class LucenceApplication implements CommandLineRunner {

    @Resource
    CreateIndexTestThread createThread;
    @Resource
    SearchIndexTestThread searchThread;

    public static void main(String[] args) {
        SpringApplication.run(LucenceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //8.2版本lucene

        //1.创建
//        Thread thcreateThread = new Thread(createThread);
//        thcreateThread.start();
//
//
//
//        while (true){
//            if(!thcreateThread.isAlive()){
//                System.out.println("【rucene文档创建完毕！】");
//                break;
//            }else{
//                System.out.println("rucene进行中。。。");
//            }
//            Thread.sleep(1000);
//        }

        //2.查询
        Thread thsearchThread = new Thread(searchThread);
        thsearchThread.start();
    }
}
