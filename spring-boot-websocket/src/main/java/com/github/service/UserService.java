package com.github.service;

import com.github.model.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {


	public User getByUsername(String username) {

		Set<String> roleSet = new HashSet<>();
		roleSet.add("ROLE_USER");
		roleSet.add("ROLE_ADMIN");

		return new User(username, roleSet);
	}

}
