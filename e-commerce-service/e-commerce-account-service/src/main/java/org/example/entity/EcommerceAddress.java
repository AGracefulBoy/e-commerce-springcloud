package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.esf.OtherHash;
import org.example.account.AddressInfo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户地址表实体类定义
 * @author zhoudashuai
 * @date 2021年12月21日 10:57 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_ecommerce_address")
public class EcommerceAddress{
    /**自增主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**用户id*/
    @Column(name = "user_id",nullable = false)
    private Long userId;

    /**用户姓名*/
    @Column(name = "username",nullable = false)
    private String username;

    /**电话号*/
    @Column(name = "phone",nullable = false)
    private String phone;

    /**省份*/
    @Column(name = "province",nullable = false)
    private String province;

    /**城市*/
    @Column(name = "city",nullable = false)
    private String city;

    /**地址详细信息*/
    @Column(name = "address_detail",nullable = false)
    private String addressDetail;

    /**创建时间*/
    @CreatedDate
    @Column(name = "create_time",nullable = false)
    private Date createTime;

    /**更新时间*/
    @LastModifiedDate
    @Column(name = "update_time",nullable = false)
    private Date updateTime;

    /*
     * 根据userid + AddressItem 得到EcommerceAddress
     * @author zhoudashuai
     * @date 2021/12/21 11:24 下午
     * @param null
     * @return null
     */
    public static EcommerceAddress to(Long userId, AddressInfo.AddressItem addressItem){
        EcommerceAddress ecommerceAddress = new EcommerceAddress();

        ecommerceAddress.setUserId(userId);
        ecommerceAddress.setUsername(addressItem.getUsername());
        ecommerceAddress.setPhone(addressItem.getPhone());
        ecommerceAddress.setProvince(addressItem.getProvince());
        ecommerceAddress.setCity(addressItem.getCity());
        ecommerceAddress.setAddressDetail(addressItem.getAddressDetail());

        return ecommerceAddress;
    }

    /**
     * 将EcommerceAddress 转为AddressItem
     * @return
     */
    public AddressInfo.AddressItem toAddressItem(){
        AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();

        addressItem.setId(this.id);
        addressItem.setUsername(this.username);
        addressItem.setPhone(this.getPhone());
        addressItem.setAddressDetail(this.addressDetail);
        addressItem.setProvince(this.province);
        addressItem.setCity(this.city);
        addressItem.setCreateTime(this.createTime);
        addressItem.setUpdateTime(this.updateTime);

        return addressItem;
    }
}
