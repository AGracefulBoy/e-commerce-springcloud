package org.example.service.communication.hystrix;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.service.communication.AuthorityFeignClient;
import org.example.vo.JwtToken;
import org.example.vo.UserAndPassword;
import org.springframework.stereotype.Component;

/**
 * 后备fallback
 * @author zhoudashuai
 * @date 2022年04月17日 4:28 下午
 */
@Slf4j
@Component
public class AuthorityFeignClientFallback implements AuthorityFeignClient {
    @Override
    public JwtToken getTokenByFeign(UserAndPassword userAndPassword) {
        log.info("authority feign client get token by feign request error " +
                "Hystrix fallback", JSON.toJSONString(userAndPassword));
        return new JwtToken("zhoudahsuai");
    }
}
