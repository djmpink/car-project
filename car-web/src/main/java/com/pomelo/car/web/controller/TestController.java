package com.pomelo.car.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pomelo.car.web.model.User;
import com.pomelo.car.web.service.UserService;

@Controller
public class TestController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value ="test.do",method = {RequestMethod.GET,RequestMethod.POST})
	public String test(ModelMap map){
		System.out.println("do it===========");
		User user = userService.findAllUser().get(0);
		map.put("user", user);
		return "test.jsp";
	}
}
