package org.example.dao;

import org.example.entity.EcommerceBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhoudashuai
 * @date 2021年12月21日 11:47 下午
 */
public interface EcommerceBalanceDao extends JpaRepository<EcommerceBalance,Long> {

    /**
     * 根据userid 查询 用户信息
     * @param userId
     * @return
     */
    EcommerceBalance findByUserId(Long userId);
}
