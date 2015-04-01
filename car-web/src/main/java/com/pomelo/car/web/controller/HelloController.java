package com.pomelo.car.web.controller;

import com.pomelo.car.web.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by zl on 2015/3/31.
 */
@Controller
public class HelloController {

    // http://127.0.0.1:8081/hello.do?name=zl
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value = "hello.do", method = RequestMethod.GET)
    @ResponseBody
    public User hello(){
        logger.info("Hello spring MVC！");
        User user =new User();
        user.setAge(20);
        user.setName("zl");
        return user;
    }
    //    @RequestMapping(value = "hello.do", method = RequestMethod.GET)
//    public void hello(HttpServletResponse response,String name) throws IOException {
//        System.out.println("Hello spring MVC！");
//        Map map=new HashMap<String,String>();
//        map.put("name1","lizuoqing");
//        map.put("name2",name);
//        response.getWriter().println(map);
//        response.getWriter().close();
//    }
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}
