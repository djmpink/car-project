package com.pomelo.car.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pomelo.car.web.dao.UserDao;
import com.pomelo.car.web.model.User;
@Component
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public List<User> findAllUser(){
		return userDao.getAllUsers();
	}
}
