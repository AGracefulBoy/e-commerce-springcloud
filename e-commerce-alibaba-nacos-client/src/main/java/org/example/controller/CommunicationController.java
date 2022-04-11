package org.example.controller;

import org.example.service.communication.UserRestTemplateService;
import org.example.vo.JwtToken;
import org.example.vo.UserAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoudashuai
 * @date 2022年04月10日 2:13 下午
 */
@RestController
@RequestMapping("/communication")
public class CommunicationController {

    private final UserRestTemplateService userRestTemplateService;

    public CommunicationController(UserRestTemplateService userRestTemplateService) {
        this.userRestTemplateService = userRestTemplateService;
    }

    @PostMapping("rest-template")
    public JwtToken getTokenFromAuthority(@RequestBody UserAndPassword userAndPassword){
        return userRestTemplateService.getTokenFromAuthorityService(userAndPassword);
    }

    @PostMapping("rest-template-load-balance")
    public JwtToken getTokenFromAuthorityWithLoadBalance(@RequestBody UserAndPassword userAndPassword){
        return userRestTemplateService.getTokenFromAuthorityServiceWithLoadBalance(userAndPassword);
    }

}
