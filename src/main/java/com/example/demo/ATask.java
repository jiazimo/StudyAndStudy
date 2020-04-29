package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

/**
 * Author: YaoQi
 * Date: 2018/9/28 21:40
 * Description: Scheduled Task
 */
@Service
public class ATask {

    private static final Logger logger = LoggerFactory.getLogger(ATask.class);
    
    @Scheduled(cron = "*/1 * * * * ?")
    public void execute() {
        logger.info("print word.");
        logger.info(String.valueOf(System.currentTimeMillis()));
    }
    
    public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.137.30",6379);
		String in = jedis.get("hello");
		System.out.println(in);
		
	}
}