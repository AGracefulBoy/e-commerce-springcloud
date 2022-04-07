package org.example.service;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.EcommerceUserDao;
import org.example.entity.EcommerceUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class EcommerceUserTest {
    @Autowired
    private EcommerceUserDao ecommerceUserDao;

    //@Test
    public void createUserRecord(){
        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername("zhoudashuai.com");
        ecommerceUser.setPassword(MD5.create().digestHex("123456"));
        ecommerceUser.setExtraInfo("{}");
        log.info("save user: [{}]", JSON.toJSON(ecommerceUserDao.save(ecommerceUser)));
    }
}
