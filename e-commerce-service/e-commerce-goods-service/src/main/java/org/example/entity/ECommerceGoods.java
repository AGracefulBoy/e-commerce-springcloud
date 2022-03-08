package org.example.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constant.BrandCategory;
import org.example.constant.GoodsCategory;
import org.example.constant.GoodsStatus;
import org.example.converter.BrandCategoryConverter;
import org.example.converter.GoodsCategoryConverter;
import org.example.converter.GoodsStatusConverter;
import org.example.goods.GoodsInfo;
import org.example.goods.SimpleGoodsInfo;
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

    public static ECommerceGoods to(GoodsInfo goodsInfo){
        ECommerceGoods ecommerceGoods = new ECommerceGoods();
        ecommerceGoods.setGoodsCategory(GoodsCategory.of(goodsInfo.getGoodsCategory()));
        ecommerceGoods.setBrandCategory(BrandCategory.of(goodsInfo.getBrandCategory()));
        ecommerceGoods.setGoodsName(goodsInfo.getGoodsName());
        ecommerceGoods.setGoodsPic(goodsInfo.getGoodsPic());
        ecommerceGoods.setGoodsDescription(goodsInfo.getGoodsDescription());
        ecommerceGoods.setGoodsStatus(GoodsStatus.ONLINE);
        ecommerceGoods.setPrice(goodsInfo.getPrice());
        ecommerceGoods.setSupply(goodsInfo.getSupply());
        ecommerceGoods.setInventory(goodsInfo.getInventory());
        ecommerceGoods.setGoodsProperty(
                JSON.toJSONString(goodsInfo.getGoodsProperty())
        );
        return ecommerceGoods;
    }

    /**
     * 将实体对象转为 GoodsInfo对象
     * @return
     */
    public GoodsInfo toGoodsInfo(){
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setId(this.id);
        goodsInfo.setGoodsCategory(this.goodsCategory.getCode());
        goodsInfo.setBrandCategory(this.brandCategory.getCode());
        goodsInfo.setGoodsPic(this.goodsPic);
        goodsInfo.setGoodsName(this.getGoodsName());
        goodsInfo.setGoodsDescription(this.getGoodsDescription());
        goodsInfo.setGoodsStatus(this.getGoodsStatus().getStatus());
        goodsInfo.setPrice(this.price);
        goodsInfo.setGoodsProperty(
                JSON.parseObject(this.goodsProperty,GoodsInfo.GoodsProperty.class)
        );
        goodsInfo.setSupply(this.supply);
        goodsInfo.setInventory(this.inventory);
        goodsInfo.setCreateTime(this.createTime);
        goodsInfo.setUpdateTime(this.updateTime);
        return goodsInfo;
    }

    /**
     * 将实体对象进行转换
     * @return
     */
    public SimpleGoodsInfo toSimpleInfo(){
        SimpleGoodsInfo simpleGoodsInfo = new SimpleGoodsInfo();
        simpleGoodsInfo.setId(this.id);
        simpleGoodsInfo.setGoodsName(this.getGoodsName());
        simpleGoodsInfo.setGoodsPic(this.getGoodsPic());
        simpleGoodsInfo.setPrice(this.price);
        return simpleGoodsInfo;
    }
}
