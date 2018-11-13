package com.github;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CasClientConfig {

	private String casServerUrlPrefix = "https://i.chaoxing.com";
	private String casServerLoginUrl = casServerUrlPrefix + "/login"; // casServerLoginUrl:cas服务的登录url
	private String casServerLogoutUrl = casServerUrlPrefix + "/logout";
	private String serverName = "http://i.moonsinfo.com"; // 没有最后的/, 相当于是域名前缀
	private Boolean casEnabled = true;

	public String getServerName() {
		return serverName;
	}
	public String getCasServerLogoutUrl() {
		return casServerLogoutUrl;
	}

	/**
	 * 用于实现单点登出功能
	 */
	@Bean
	public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
		ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
		listener.setEnabled(casEnabled);
		listener.setListener(new SingleSignOutHttpSessionListener());
		listener.setOrder(1);
		return listener;
	}

	/**
	 * 集成 spring security
	 * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
	 * import org.springframework.security.web.authentication.logout.LogoutFilter;
	 import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

	 @Bean public FilterRegistrationBean logOutFilter() {
	 FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
	 LogoutFilter logoutFilter = new LogoutFilter(autoconfig.getCasServerUrlPrefix() + "/logout?service=" + autoconfig.getServerName(), new SecurityContextLogoutHandler());
	 filterRegistration.setFilter(logoutFilter);
	 filterRegistration.setEnabled(casEnabled);
	 filterRegistration.addUrlPatterns("/logout");
	 filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
	 filterRegistration.addInitParameter("serverName", serverName);
	 filterRegistration.setOrder(2);
	 return filterRegistration;
	 }*/

	/**
	 * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
	 */
	@Bean
	public FilterRegistrationBean singleSignOutFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new SingleSignOutFilter());
		filterRegistration.setEnabled(casEnabled);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
		filterRegistration.addInitParameter("serverName", serverName);
		filterRegistration.setOrder(3);
		return filterRegistration;
	}

	/**
	 * 该过滤器负责用户的认证工作
	 */
	@Bean
	public FilterRegistrationBean authenticationFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new AuthenticationFilter());
		filterRegistration.setEnabled(casEnabled);
		filterRegistration.addUrlPatterns("/login");
		filterRegistration.addInitParameter("casServerLoginUrl", casServerLoginUrl);
		filterRegistration.addInitParameter("serverName", serverName);
		filterRegistration.setOrder(4);
		return filterRegistration;
	}

	/**
	 * 该过滤器负责对Ticket的校验工作
	 */
	@Bean
	public FilterRegistrationBean cas30ProxyReceivingTicketValidationFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		Cas30ProxyReceivingTicketValidationFilter cas30ProxyReceivingTicketValidationFilter = new Cas30ProxyReceivingTicketValidationFilter();
		cas30ProxyReceivingTicketValidationFilter.setServerName(serverName);
		filterRegistration.setFilter(cas30ProxyReceivingTicketValidationFilter);
		filterRegistration.setEnabled(casEnabled);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
		filterRegistration.addInitParameter("serverName", serverName);
		filterRegistration.setOrder(5);
		return filterRegistration;
	}


	/**
	 * 该过滤器对HttpServletRequest请求包装， 可通过HttpServletRequest的getRemoteUser()方法获得登录用户的登录名
	 */
	@Bean
	public FilterRegistrationBean httpServletRequestWrapperFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new HttpServletRequestWrapperFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setOrder(6);
		return filterRegistration;
	}

	/**
	 * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
	 * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
	 * 这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
	 */
	@Bean
	public FilterRegistrationBean assertionThreadLocalFilter() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new AssertionThreadLocalFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setOrder(7);
		return filterRegistration;
	}


}