package com.moon.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Moon on 12/10/2020
 */

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping(path="login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping(path = "courses")
    public String getCourses(){
        return "courses";
    }
}
