package org.example.dao;

import org.example.constant.BrandCategory;
import org.example.constant.GoodsCategory;
import org.example.entity.ECommerceGoods;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * @author zhoudashuai
 * @date 2022年01月03日 8:47 下午
 */
public interface ECommerceGoodsDao extends PagingAndSortingRepository<ECommerceGoods,Long> {
    Optional<ECommerceGoods> findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(
            GoodsCategory goodsCategory, BrandCategory brandCategory,
            String goodsName
            );
}
