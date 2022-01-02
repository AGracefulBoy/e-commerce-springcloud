package org.example.dao;

import org.example.entity.EcommerceAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author zhoudashuai
 * @date 2021年12月21日 11:32 下午
 */
public interface EcommerceAddressDao extends JpaRepository<EcommerceAddress,Long> {

    /**
     * 根据 用户id查询地址信息
     * @param userId
     * @return
     */
    List<EcommerceAddress> findAllByUserId(Long userId);
}
