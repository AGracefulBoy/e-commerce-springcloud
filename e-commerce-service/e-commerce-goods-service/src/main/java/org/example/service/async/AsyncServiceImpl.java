package org.example.service.async;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.example.constant.GoodsConstant;
import org.example.dao.ECommerceGoodsDao;
import org.example.entity.ECommerceGoods;
import org.example.goods.GoodsInfo;
import org.example.goods.SimpleGoodsInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 异步服务接口实现
 * @author zhoudashuai
 * @date 2022年01月06日 11:06 下午
 */
@Slf4j
@Service
@Transactional
public class AsyncServiceImpl implements IAsyncService{

    private final ECommerceGoodsDao eCommerceGoodsDao;

    private final StringRedisTemplate redisTemplate;

    public AsyncServiceImpl(ECommerceGoodsDao eCommerceGoodsDao, StringRedisTemplate redisTemplate) {
        this.eCommerceGoodsDao = eCommerceGoodsDao;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 异步任务需要加上注解，并指定使用的线程池
     * 异步任务处理两件事
     * 1，将商品信息保存到数据表
     * 2，更新商品缓存
     * @param goodsInfos
     * @param taskId
     */
    @Async("getAsyncExecutor")
    @Override
    public void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId) {
        log.info("async task running taskId: [{}]",taskId);

        StopWatch watch = StopWatch.createStarted();

        //1.如果是goodsInfo存在重复的商品，不保存，直接返回，记录错误日志
        //请求数据是否合法的标记
        boolean isIllegal = false;

        //将商品信息字段 joint在一起，用来判断是否存在重复
        Set<String> goodsJoinInfos = new HashSet<>(goodsInfos.size());

        //过滤出来的，可以入库的商品信息

        List<GoodsInfo> filterGoodsInfo = new ArrayList<>(goodsInfos.size());

        //走一遍循环，过滤非法参数与判定当前请求是否合法
        for (GoodsInfo goods : goodsInfos) {
            //基本条件不满足的，直接过滤
            if (goods.getPrice() <= 0 || goods.getSupply() <=0){
                log.info("goods info is invalid: [{}]", JSON.toJSONString(goods));
                continue;
            }

            //组合每一个商品信息
            String jointInfo = String.format(
                    "%s,%s,%s",
                    goods.getGoodsCategory(),goods.getBrandCategory(),goods.getGoodsName()
            );

            if (goodsJoinInfos.contains(jointInfo)){
                isIllegal = true;
            }

            //加入到两个容器中
            goodsJoinInfos.add(jointInfo);

            filterGoodsInfo.add(goods);
        }
        //如果存在重复商品，获取没有入库的商品，直接打印日志返回
        if (isIllegal || CollectionUtils.isEmpty(filterGoodsInfo)){
            watch.stop();
            log.warn("import noting: [{}]",JSON.toJSONString(filterGoodsInfo));
            log.info("check and import goods done: [{}ms]",
                    watch.getTime(TimeUnit.MILLISECONDS));
            return;
        }

        List<ECommerceGoods> eCommerceGoods = filterGoodsInfo.stream()
                .map(ECommerceGoods::to)
                .collect(Collectors.toList());

        List<ECommerceGoods> targetGoods = new ArrayList<>(eCommerceGoods.size());

        //2.保存goodsInfo先去判断是否重复商品
        eCommerceGoods.forEach(g -> {
            if (null != eCommerceGoodsDao
                    .findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(
                            g.getGoodsCategory(),g.getBrandCategory(),g.getGoodsName()
                    ).orElse(null)){
                return;
            }
            targetGoods.add(g);
        });

        //商品信息入库
        List<ECommerceGoods> saveGoods = IterableUtils.toList(
                eCommerceGoodsDao.saveAll(targetGoods)
        );
        saveNewGoodsInfoToRedis(saveGoods);
        //TODO将入库商品信息同步到 redis中
        log.info("save goods info to db and redis: [{}]",saveGoods.size());
        watch.stop();
        log.info("check and import goods success : [{}ms]",watch.getTime(TimeUnit.MILLISECONDS));
    }

    /**
     * 将保存到数据表中的数据缓存到redis中
     * @param savedGoods
     */
    private void saveNewGoodsInfoToRedis(List<ECommerceGoods> savedGoods){
        //由于redis是内存存储，只存储简单商品信息
        List<SimpleGoodsInfo> simpleGoodsInfos = savedGoods.stream()
                .map(ECommerceGoods::toSimpleInfo)
                .collect(Collectors.toList());

        Map<String,String> id2JsonObject = new HashMap<>(simpleGoodsInfos.size());

        simpleGoodsInfos.forEach(
                g -> id2JsonObject.put(g.getId().toString(),JSON.toJSONString(g))
        );

        //保存到redis中
        redisTemplate.opsForHash().putAll(
                GoodsConstant.ECOMMERCE_GOODS_DICT_KEY,
                id2JsonObject
        );
    }
}
