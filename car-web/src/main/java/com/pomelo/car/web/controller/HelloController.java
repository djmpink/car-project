package com.pomelo.car.web.controller;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by zl on 2015/3/31.
 */
@Controller
public class HelloController {
    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String hello(){
        System.out.println("Hello spring MVC！");

        //String jsonString = JSON.toJSONString(person, SerializerFeature.PrettyFormat);

        JSONObject jsonObject =new JSONObject();
        jsonObject.put("name", "zhoul");
        jsonObject.put("age",26);

        return "index";
    }
}
