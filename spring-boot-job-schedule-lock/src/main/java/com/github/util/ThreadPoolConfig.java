package com.github.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;


/** Bean配置管理 */
@EnableAsync
@Configuration
@EnableScheduling
public class ThreadPoolConfig extends AsyncConfigurerSupport implements SchedulingConfigurer, InitializingBean, DisposableBean {

	private static ThreadPoolTaskScheduler taskScheduler = null;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void afterPropertiesSet() {
		taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
		taskScheduler.setAwaitTerminationSeconds(60 * 60);
		taskScheduler.setThreadNamePrefix("executor-");
		taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
		taskScheduler.initialize();
	}
	@Override
	public void destroy() {
		taskScheduler.shutdown();
	}

	@Override
	public Executor getAsyncExecutor() {
		return taskScheduler;
	}

	@Bean
	public Executor executor() {
		return taskScheduler;
	}
	@Bean
	public TaskExecutor taskExecutor() {
		return taskScheduler;
	}
	@Bean
	public TaskScheduler taskScheduler() {
		return taskScheduler;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler);
	}

	@Bean
	public Object threadPoolMonitor() {
		return new Object() {
			ScheduledThreadPoolExecutor executor = taskScheduler.getScheduledThreadPoolExecutor();
			private static final String MONITOR_MESSAGE = "[%s monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, Queue.size: %d, isShutdown: %s, isTerminated: %s";
			@Scheduled(fixedDelay = 30 * 1000)
			public void run() {
				String message = String.format(MONITOR_MESSAGE,
						"executor",
						executor.getPoolSize(), executor.getCorePoolSize(),
						executor.getActiveCount(), executor.getCompletedTaskCount(),
						executor.getTaskCount(), executor.getQueue().size(),
						executor.isShutdown(), executor.isTerminated());
				logger.info(message);
			}
		};
	}

}

