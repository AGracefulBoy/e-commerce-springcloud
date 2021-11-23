package org.example.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.checkerframework.checker.units.qual.A;
import org.example.util.TokenParseUtil;
import org.example.vo.LoginUserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * JWT 相关服务测试类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class JWTServiceTest {
    @Autowired
    private IJWTService ijwtService;

    @Test
    public void testGenerateAndParseToken() throws Exception{
        String jwtToken = ijwtService.generateToken("zhoudashuai.com","e10adc3949ba59abbe56e057f20f883e");
        log.info("jwt token is : [{}]",jwtToken);

        LoginUserInfo userInfo = TokenParseUtil.parseUserInfoFromToken(jwtToken);
        log.info("parse token: [{}]", JSON.toJSONString(userInfo));
    }
}
