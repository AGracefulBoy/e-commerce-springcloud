package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author zhoudashuai
 * @date 2021年12月21日 11:37 下午
 */
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_ecommerce_balance")
public class EcommerceBalance {

    /** 自增id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /** 用户id */
    @Column(name = "user_id",nullable = false)
    private Long userId;

    /** 用户余额 */
    @Column(name = "balance",nullable = false)
    private Long balance;

    /** 创建时间 */
    @Column(name = "create_time",nullable = false)
    @CreatedDate
    private Date createTime;

    /** 更新时间 */
    @Column(name = "update_time",nullable = false)
    @LastModifiedDate
    private Date updateTime;
}
