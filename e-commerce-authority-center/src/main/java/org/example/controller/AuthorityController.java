package org.example.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.IgnoreResponseAdvice;
import org.example.service.IJWTService;
import org.example.vo.JwtToken;
import org.example.vo.UserAndPassword;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对外暴露的授权服务接口
 * @author zhouguobing
 */
@RestController
@Slf4j
@RequestMapping("/authority")
public class AuthorityController {

    private final IJWTService ijwtService;

    public AuthorityController(IJWTService ijwtService) {
        this.ijwtService = ijwtService;
    }

    /**
     * 从授权中心获取Token 其实就是登录功能 且返回信息中没有统一响应的包中
     * @param userAndPassword
     * @return
     */
    @IgnoreResponseAdvice
    @PostMapping("/token")
    public JwtToken token(@RequestBody UserAndPassword userAndPassword) throws Exception{
        log.info("request to get token with param: [{}]", JSON.toJSONString(userAndPassword));

        return new JwtToken(ijwtService.generateToken(
                userAndPassword.getUsername(), userAndPassword.getPassword()
        ));
    }

    /**
     * 注册用户并返回当前注册用户的token，即通过授权中心创建用户
     * @param userAndPassword
     * @return
     * @throws Exception
     */
    @IgnoreResponseAdvice
    @PostMapping("/register")
    public JwtToken register(@RequestBody UserAndPassword userAndPassword) throws Exception{

        log.info("register user with param : [{}]",JSON.toJSONString(userAndPassword));

        return new JwtToken(ijwtService.registerUserAndGenerateToken(userAndPassword));
    }
}
