package org.example.service.impl;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.example.constant.AuthorityConstant;
import org.example.constant.CommonConstant;
import org.example.dao.EcommerceUserDao;
import org.example.entity.EcommerceUser;
import org.example.service.IJWTService;
import org.example.vo.LoginUserInfo;
import org.example.vo.UserAndPassword;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IJWTServiceImpl implements IJWTService {

    private final EcommerceUserDao ecommerceUserDao;

    public IJWTServiceImpl(EcommerceUserDao ecommerceUserDao) {
        this.ecommerceUserDao = ecommerceUserDao;
    }

    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username,password,0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {

        //首先需要验正是否能够通过授权校验，即输入的用户名和密码能否匹配数据库表记录
        EcommerceUser ecommerceUser = ecommerceUserDao.findByUsernameAndPassword(username,password);

        if (null == ecommerceUser){
            log.error("can not find user: [{}] ,[{}]:",username,password);
            return null;
        }

        //Token 中塞入对象,即JWT 中的存储的信息,后端拿到这些信息就可以知道是哪个用户在操作
        LoginUserInfo loginUserInfo = new LoginUserInfo(ecommerceUser.getId(),ecommerceUser.getUsername());

        if (expire <= 0){
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }

        ZonedDateTime zdt = LocalDate.now().plus(expire, ChronoUnit.DAYS)
                .atStartOfDay(ZoneId.systemDefault());

        Date expireDate = Date.from(zdt.toInstant());

        return Jwts.builder()
                // jwt payload - KV
                .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                .setId(UUID.randomUUID().toString())
                // 过期时间
                .setExpiration(expireDate)
                // jwt 签名 rsa
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String registerUserAndGenerateToken(UserAndPassword userAndPassword) throws Exception {

        //先去校验用户名是否存在,如果存在，不能重复注册
        EcommerceUser oldUser = ecommerceUserDao.findByUsername(userAndPassword.getUsername());

        if (null != oldUser){
            log.error("username is registered: [{}]",oldUser.getUsername());
            return null;
        }

        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername(userAndPassword.getUsername());
        ecommerceUser.setPassword(userAndPassword.getPassword());
        ecommerceUser.setExtraInfo("{}");

        //注册一个新用户到注册表中
        ecommerceUser = ecommerceUserDao.save(ecommerceUser);

        log.info("register user success: [{}] [{}]:" ,ecommerceUser.getUsername(),ecommerceUser.getId());

        //生成token 并返回
        return generateToken(ecommerceUser.getUsername(),ecommerceUser.getPassword());
    }

    /**
     * 根据本地存储的私钥，获取PrivateKey 对象
     * @return
     * @throws Exception
     */
    private PrivateKey getPrivateKey() throws Exception{
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
