package org.example.service;

import org.example.common.TableId;
import org.example.goods.DeductGoodsInventory;
import org.example.goods.GoodsInfo;
import org.example.goods.SimpleGoodsInfo;
import org.example.vo.PageSimpleGoodsInfo;

import java.util.List;

/**
 * 商品微服务相关服务接口定义
 * @author zhoudashuai
 * @date 2022年01月04日 11:32 下午
 */
public interface IGoodsService {

    /**
     * 根据tableId查询
     * @param tableId
     * @return
     */
    List<GoodsInfo> getGoodsInfoByTableId(TableId tableId);

    PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page);

    /**
     * 获取简单商品信息
     * @param tableId
     * @return
     */
    List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId);

    /**
     * 扣减商品库存
     * @param deductGoodsInventories
     * @return
     */
    Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories);
}
