package org.example.service.communication.hystrix;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.example.service.communication.AuthorityFeignClient;
import org.example.vo.JwtToken;
import org.example.vo.UserAndPassword;
import org.springframework.stereotype.Component;

/**
 * openfeign继承hystrix的另一种模式
 * @author zhoudashuai
 * @date 2022年04月17日 5:03 下午
 */
@Slf4j
@Component
public class AuthorityFeignClientFallbackFactory
        implements FallbackFactory<AuthorityFeignClient> {
    @Override
    public AuthorityFeignClient create(Throwable cause) {
        log.warn("authority feign client get token by feign request error" +
                "hystrix fallbackFactory:[{}]",cause.getMessage(),cause);

        return new AuthorityFeignClient(){
            @Override
            public JwtToken getTokenByFeign(UserAndPassword userAndPassword) {
                return new JwtToken("zhoudashuai-factory");
            }
        };
    }
}
