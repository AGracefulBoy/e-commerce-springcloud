package org.example.service;

import org.example.vo.UserAndPassword;

/**
 * JWT相关服务接口定义
 */
public interface IJWTService {

    /**
     * 生成JWT token，使用默认的超时时间
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    String generateToken(String username,String password) throws Exception;

    /**
     * 生成指定超时时间的 token
     * @param username
     * @param password
     * @param expire
     * @return
     * @throws Exception
     */
    String generateToken(String username,String password,int expire) throws Exception;


    /**
     * 注册用户并生成Token返回
     * @param userAndPassword
     * @return
     * @throws Exception
     */
    String registerUserAndGenerateToken(UserAndPassword userAndPassword) throws Exception;
}
