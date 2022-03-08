package org.example.service.async;

import org.example.goods.GoodsInfo;

import java.util.List;

/**
 * 异步服务接口定义
 * @author zhoudashuai
 * @date 2022年01月04日 11:36 下午
 */
public interface IAsyncService {

    /**
     * 异步将商品信息保存下来
     * @param goodsInfos
     * @param taskId
     */
    void asyncImportGoods(List<GoodsInfo> goodsInfos,String taskId);
}
