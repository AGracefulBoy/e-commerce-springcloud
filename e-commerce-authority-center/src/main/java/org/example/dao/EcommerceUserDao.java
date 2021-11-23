package org.example.dao;

import org.example.entity.EcommerceUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * dao层接口定义
 */
public interface EcommerceUserDao extends JpaRepository<EcommerceUser, Long> {
    EcommerceUser findByUsername(String username);
    EcommerceUser findByUsernameAndPassword(String username,String password);
}
