/*
 * .............................................
 *
 * 				    _ooOoo_
 * 		  	       o8888888o
 * 	  	  	       88" . "88
 *                 (| -_- |)
 *                  O\ = /O
 *              ____/`---*\____
 *               . * \\| |// `.
 *             / \\||| : |||// \
 *           / _||||| -:- |||||- \
 *             | | \\\ - /// | |
 *            | \_| **\---/** | |
 *           \  .-\__ `-` ___/-. /
 *            ___`. .* /--.--\ `. . __
 *        ."" *< `.___\_<|>_/___.* >*"".
 *      | | : `- \`.;`\ _ /`;.`/ - ` : | |
 *         \ \ `-. \_ __\ /__ _/ .-` / /
 *======`-.____`-.___\_____/___.-`____.-*======
 *
 * .............................................
 *              佛祖保佑 永无BUG
 *
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 * 程序人员写程序，又拿程序换酒钱。
 * 酒醒只在网上坐，酒醉还来网下眠；
 * 酒醉酒醒日复日，网上网下年复年。
 * 但愿老死电脑间，不愿鞠躬老板前；
 * 奔驰宝马贵者趣，公交自行程序员。
 * 别人笑我忒疯癫，我笑自己命太贱；
 * 不见满街漂亮妹，哪个归得程序员？
 *
 * 北纬30.√  154518484@qq.com
 */
package com.github.controller;

import com.github.CasClientConfig;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class IndexController {

	@Resource
	private CasClientConfig casClientConfig;

	@RequestMapping({"", "/", "index"})
	public String index() {
		return "index";
	}

	@RequestMapping("login")
	public String afterCasLogin(HttpServletRequest request) {

		System.err.println(request.getRemoteUser());
		System.err.println(request.getUserPrincipal());

		System.err.println(AssertionHolder.getAssertion().getAttributes());
		System.err.println(AssertionHolder.getAssertion().getAuthenticationDate());
		System.err.println(AssertionHolder.getAssertion().getPrincipal());

		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
		System.err.println(principal.getAttributes());


		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "index";
	}

	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().invalidate();

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + casClientConfig.getCasServerLogoutUrl();
	}

	@RequestMapping("logout/callback")
	public String logoutCallback(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().invalidate();

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + casClientConfig.getCasServerLogoutUrl() + "?service=" + casClientConfig.getServerName() + "/logout/success";
	}

	@RequestMapping("logout/success")
	public String logoutSuccess() {
		System.err.println("logoutSuccess");
		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/index";
	}

}

