package com.sibro.lucence;

import com.sibro.lucence.thread.CreateIndexTestThread;
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

    public static void main(String[] args) {
        SpringApplication.run(LucenceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Thread thIndexThread = new Thread(IndexThread);
        thIndexThread.start();
    }
}
