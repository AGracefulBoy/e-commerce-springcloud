package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constant.BrandCategory;
import org.example.constant.GoodsCategory;
import org.example.constant.GoodsStatus;
import org.example.converter.BrandCategoryConverter;
import org.example.converter.GoodsCategoryConverter;
import org.example.converter.GoodsStatusConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhoudashuai
 * @date 2022年01月03日 8:36 下午
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_ecommerce_goods")
public class ECommerceGoods {

    /** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 商品类型 */
    @Column(name = "goods_category", nullable = false)
    @Convert(converter = GoodsCategoryConverter.class)
    private GoodsCategory goodsCategory;

    /** 品牌分类 */
    @Column(name = "brand_category", nullable = false)
    @Convert(converter = BrandCategoryConverter.class)
    private BrandCategory brandCategory;

    /** 商品名称 */
    @Column(name = "goods_name", nullable = false)
    private String goodsName;

    /** 商品名称 */
    @Column(name = "goods_pic", nullable = false)
    private String goodsPic;

    /** 商品描述信息 */
    @Column(name = "goods_description", nullable = false)
    private String goodsDescription;

    /** 商品状态 */
    @Column(name = "goods_status", nullable = false)
    @Convert(converter = GoodsStatusConverter.class)
    private GoodsStatus goodsStatus;

    /** 商品价格: 单位: 分、厘 */
    @Column(name = "price", nullable = false)
    private Integer price;

    /** 总供应量 */
    @Column(name = "supply", nullable = false)
    private Long supply;

    /** 库存 */
    @Column(name = "inventory", nullable = false)
    private Long inventory;

    /** 商品属性, json 字符串存储 */
    @Column(name = "goods_property", nullable = false)
    private String goodsProperty;

    /** 创建时间 */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    /** 更新时间 */
    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    private Date updateTime;
}
