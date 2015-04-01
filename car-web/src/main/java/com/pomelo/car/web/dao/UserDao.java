package com.pomelo.car.web.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pomelo.car.web.model.User; 
@Component("userDao")
public class UserDao extends BaseDao<User>{
	public UserDao() {
		super.setType(User.class);
	}
	
	public List<User> getAllUsers(){
		return getList("select * from user");
	}
	
	
}
