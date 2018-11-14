package com.github.service;

import com.github.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = this.getByUsername("iMiracle");
		if (user == null) {
			throw new UsernameNotFoundException("没有该用户不存在: " + username);
		}

		return user;
	}

	public User getByUsername(String username) {

		Set<String> roleSet = new HashSet<>();
		roleSet.add("ROLE_USER");
		roleSet.add("ROLE_ADMIN");

		return new User(username, roleSet);
	}

}
