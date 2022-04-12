package org.example.controller;

import org.example.service.communication.AuthorityFeignClient;
import org.example.service.communication.UseFeignApi;
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

    private final UseFeignApi useFeignApi;
    private final AuthorityFeignClient authorityFeignClient;
    private final UserRestTemplateService userRestTemplateService;

    public CommunicationController(UserRestTemplateService userRestTemplateService, AuthorityFeignClient authorityFeignClient, UseFeignApi useFeignApi) {
        this.userRestTemplateService = userRestTemplateService;
        this.authorityFeignClient = authorityFeignClient;
        this.useFeignApi = useFeignApi;
    }

    @PostMapping("rest-template")
    public JwtToken getTokenFromAuthority(@RequestBody UserAndPassword userAndPassword){
        return userRestTemplateService.getTokenFromAuthorityService(userAndPassword);
    }

    @PostMapping("rest-template-load-balance")
    public JwtToken getTokenFromAuthorityWithLoadBalance(@RequestBody UserAndPassword userAndPassword){
        return userRestTemplateService.getTokenFromAuthorityServiceWithLoadBalance(userAndPassword);
    }

    @PostMapping("/token-by-feign")
    public JwtToken getTokenByFeign(@RequestBody UserAndPassword userAndPassword){
        return authorityFeignClient.getTokenByFeign(userAndPassword);
    }

    @PostMapping("/thinking-in-feign")
    public JwtToken thinkingInFeign(@RequestBody UserAndPassword userAndPassword){
        return useFeignApi.thinkingInFeign(userAndPassword);
    }

}
