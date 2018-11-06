package com.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DomainUserDetailsService {

	public static void main(String[] args) {

		Logger logger = LoggerFactory.getLogger("test");

		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String encode = passwordEncoder.encode("password");

		logger.info("加密后的密码:" + encode);
		logger.info("bcrypt密码对比:" + passwordEncoder.matches("password", encode));

		String md5Password = "{MD5}88e2d8cd1e92fd5544c8621508cd706b";//MD5加密前的密码为:password
		logger.info("MD5密码对比:" + passwordEncoder.matches("password", encode));
	}

}
