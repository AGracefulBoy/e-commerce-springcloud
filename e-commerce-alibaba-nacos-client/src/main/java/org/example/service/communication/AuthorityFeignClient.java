package org.example.service.communication;

import org.example.vo.JwtToken;
import org.example.vo.UserAndPassword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * contextId 相当于是每一个FeignClient的唯一标识
 * @author zhoudashuai
 * @date 2022年04月11日 9:44 下午
 */
@FeignClient(contextId = "AuthorityFeignClient",value = "e-commerce-authority-center")
public interface AuthorityFeignClient {

    /**
     * 通过OpenFeign访问Authority 获取token
     * @param userAndPassword
     * @return
     */
    @RequestMapping(value = "/ecommerce-authority-center/authority/token",
    method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    JwtToken getTokenByFeign(@RequestBody UserAndPassword userAndPassword);
}
