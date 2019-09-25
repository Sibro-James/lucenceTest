package com.sibro.lucence;

import com.sibro.lucence.thread.CreateIndexTestThread;
import com.sibro.lucence.thread.CreateIndexTestThread8;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
@MapperScan(basePackages = {"com.sibro.lucence.mapper"})
public class LucenceApplication implements CommandLineRunner {

    @Resource
    CreateIndexTestThread IndexThread;
    @Resource
    CreateIndexTestThread8 IndexThread8;

    public static void main(String[] args) {
        SpringApplication.run(LucenceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //4.10版本lunce
        Thread thIndexThread = new Thread(IndexThread);
        thIndexThread.start();


        //8.20版本lunce
//        Thread thIndexThread8 = new Thread(IndexThread8);
//        thIndexThread8.start();
    }
}
