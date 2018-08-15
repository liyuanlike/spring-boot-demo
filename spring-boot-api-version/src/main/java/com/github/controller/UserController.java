package com.github.controller;

import com.github.util.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiVersion(1)
@RestController
@RequestMapping("{version}/user")
public class UserController {

    @RequestMapping("hello")
    public String hello() {
        System.out.println("haha1..........");

        return "hello";
    }

    @ApiVersion(2)
    @RequestMapping("hello")
    public String hello2() {
        System.out.println("haha2.........");
        
        return "hello";
    }

    @ApiVersion(5)
    @RequestMapping("hello")
    public String hello5() {
        System.out.println("haha5.........");
        
        return "hello";
    }
    
}
