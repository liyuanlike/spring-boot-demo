package com.github;

import com.moonsinfo.redis.distribute.lock.spring.boot.autoconfigure.lock.RedisLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@SpringBootApplication
public class Application {

    @Resource private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RedisLock(key = "'redis:lock:job'")
    @Scheduled(cron = "0/2 * * * * ?")
    public void job() throws InterruptedException {

        BoundValueOperations<String, String> boundValueOperations = stringRedisTemplate.boundValueOps("redis:lock:count");

        String countString = boundValueOperations.get();
        if (StringUtils.isEmpty(countString)) {
            countString = "0";
        }
        Integer count = Integer.parseInt(countString);
        boundValueOperations.set(String.valueOf(count + 1));

        System.err.println(boundValueOperations.get() + ": " + Thread.currentThread());
        TimeUnit.SECONDS.sleep(2L);
    }

}






