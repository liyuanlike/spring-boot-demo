package com.github;

import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@EnableScheduling
@SpringBootApplication
public class Application {

	@Resource private RedissonClient redissonClient;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner run(final RedissonClient redissonClient) throws Exception {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {

				System.err.println(redissonClient);

			}
		};
	}
}

