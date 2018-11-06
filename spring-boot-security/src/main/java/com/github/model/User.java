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
package com.github.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class User implements UserDetails {

	private static final long serialVersionUID = -6997277893795640866L;

	private Integer id;
	private String username;
	private String password;

	private Integer passwordExpire;
	private Integer expire;
	private Integer lock;
	private Integer enable;

	public User(){}

	public User(String username) {
		this.username = username;
		this.password = "password";
		this.id = 100;
		this.passwordExpire = 0;
		this.expire = 0;
		this.lock = 0;
		this.enable = 1;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 返回分配给用户的角色列表
		return null;
	}
	@Override
	public String getPassword() {
		return this.password;
	}
	@Override
	public String getUsername() {
		return this.username;
	}
	@Override
	public boolean isAccountNonExpired() {
		// 账户是否未过期
		return false;
	}
	@Override
	public boolean isAccountNonLocked() {
		// 账户是否未锁定
		return false;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// 密码是否未过期
		return false;
	}
	@Override
	public boolean isEnabled() {
		// 账户是否激活/启用
		return false;
	}


}

