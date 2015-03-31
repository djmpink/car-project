package com.pomelo.car.web.controller;

import com.pomelo.car.web.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by zl on 2015/3/31.
 */
@Controller
public class HelloController {

    // http://127.0.0.1:8081/hello.do?name=zl
    @RequestMapping(value = "hello.do", method = RequestMethod.GET)
    public void hello(HttpServletResponse response,String name) throws IOException {
        System.out.println("Hello spring MVC！");
        Map map=new HashMap<String,String>();
        map.put("name1","lizuoqing");
        map.put("name2",name);
        response.getWriter().println(map);
        response.getWriter().close();
    }

//    @RequestMapping(value = "hello.do", method = RequestMethod.GET)
//    @ResponseBody
//    public User hello(){
//        System.out.println("Hello spring MVC！");
//
//        //String jsonString = JSON.toJSONString(person, SerializerFeature.PrettyFormat);
//
////        JSONObject jsonObject =new JSONObject();
////        jsonObject.put("name", "zhoul");
////        jsonObject.put("age",26);
//
//        User user =new User();
//        user.setAge(20);
//        user.setName("zl");
//        return user;
//    }



}
