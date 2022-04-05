package org.example.service.impl;

import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.example.common.TableId;
import org.example.constant.GoodsConstant;
import org.example.dao.ECommerceGoodsDao;
import org.example.entity.ECommerceGoods;
import org.example.goods.DeductGoodsInventory;
import org.example.goods.GoodsInfo;
import org.example.goods.SimpleGoodsInfo;
import org.example.service.IGoodsService;
import org.example.vo.PageSimpleGoodsInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhoudashuai
 * @date 2022年04月05日 7:25 下午
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements IGoodsService {

    private final StringRedisTemplate redisTemplate;

    private final ECommerceGoodsDao eCommerceGoodsDao;

    public GoodsServiceImpl(StringRedisTemplate redisTemplate, ECommerceGoodsDao eCommerceGoodsDao) {
        this.redisTemplate = redisTemplate;
        this.eCommerceGoodsDao = eCommerceGoodsDao;
    }

    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {
        //详细的商品信息不能从redis cache中去拿

        List<Long> ids = tableId.getIds().stream().map(TableId.Id::getId).collect(Collectors.toList());
        log.info("get goods info by ids: [{}]", JSON.toJSONString(ids));

        List<ECommerceGoods> eCommerceGoods = IterableUtils.toList(eCommerceGoodsDao.findAllById(ids));

        return eCommerceGoods.stream().map(ECommerceGoods::toGoodsInfo).collect(Collectors.toList());
    }

    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page) {
        //分页不能从redis cache中拿
        if (page <= 1) {
            page = 1;
        }

        Pageable pageable = PageRequest.of(
                page - 1, 10, Sort.by("id").descending()
        );

        Page<ECommerceGoods> orderPage = eCommerceGoodsDao.findAll(pageable);

        //是否还有更多页： 总页数是否大于当前给定的页
        boolean hasMore = orderPage.getTotalPages() > page;

        return new PageSimpleGoodsInfo(orderPage.getContent().stream()
                .map(ECommerceGoods::toSimpleInfo).collect(Collectors.toList()),
                hasMore);
    }

    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {

        //获取商品的简单信息，可以从redis中拿，如果拿不到，需要从DB中获取
        //redis 中的kv都是字符串
        List<Object> goodIds = tableId.getIds().stream().map(i -> i.getId().toString()).collect(Collectors.toList());
        List<Object> cachedSimpleGoodsInfo = redisTemplate.opsForHash()
                .multiGet(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, goodIds);

        //如果从redis中查到了商品信息，分两种情况取操作
        if (CollectionUtils.isNotEmpty(cachedSimpleGoodsInfo)) {
            //1.如果从缓存中查询到所有需要的 SimpleGoodsInfo
            if (cachedSimpleGoodsInfo.size() == goodIds.size()) {
                log.info("get simple goods info by ids (from cache): [{}]",
                        JSON.toJSONString(goodIds));
                return parseCachedGoodsInfo(cachedSimpleGoodsInfo);
            } else {
                // 一部分从数据表中获取，一部分去redis中获取
                List<SimpleGoodsInfo> left = parseCachedGoodsInfo(cachedSimpleGoodsInfo);
                //取差集:传递进来的参数 - 缓存中查到的 = 缓存中没有的
                Collection<Long> subtractIds = CollectionUtils.subtract(
                        goodIds.stream()
                                .map(g -> Long.valueOf(g.toString())).collect(Collectors.toList()),
                        left.stream().map(SimpleGoodsInfo::getId).collect(Collectors.toList())
                );
                //缓存中没有的，查询数据表并缓存
                List<SimpleGoodsInfo> right = queryGoodsFromDBAndCacheToRedis(new TableId(subtractIds.stream()
                        .map(TableId.Id::new).collect(Collectors.toList())));
                //合并right，left
                log.info("get simple goods info by ids ( from db and cache): [{}]", JSON.toJSONString(subtractIds));
                return new ArrayList<>(CollectionUtils.union(left, right));
            }
        }
        //从redis中什么都没查到
        return queryGoodsFromDBAndCacheToRedis(tableId);
    }

    /**
     * 将缓存中的数据反序列化为 pojo对象
     *
     * @param cachedSimpleGoodsInfo
     * @return
     */
    private List<SimpleGoodsInfo> parseCachedGoodsInfo(List<Object> cachedSimpleGoodsInfo) {
        return cachedSimpleGoodsInfo.stream().map(s -> JSON.parseObject(s.toString(), SimpleGoodsInfo.class))
                .collect(Collectors.toList());
    }

    /**
     * 从数据表中查询数据，并缓存到redis中
     *
     * @param tableId
     * @return
     */
    private List<SimpleGoodsInfo> queryGoodsFromDBAndCacheToRedis(TableId tableId) {
        // 从数据表中查询数据并做转换
        List<Long> ids = tableId.getIds().stream().map(TableId.Id::getId).collect(Collectors.toList());
        log.info("get simple goods ingo by ids (from db): [{}]",
                JSON.toJSONString(ids));
        List<ECommerceGoods> eCommerceGoods = IterableUtils.toList(eCommerceGoodsDao.findAllById(ids));
        //将结果缓存，下次可以直接从redis中拿
        List<SimpleGoodsInfo> result = eCommerceGoods.stream()
                .map(ECommerceGoods::toSimpleInfo)
                .collect(Collectors.toList());
        HashMap<String, String> id2JsonObject = new HashMap<>();
        result.forEach(g -> id2JsonObject.put(g.getId().toString(), JSON.toJSONString(g)));

        redisTemplate.opsForHash().putAll(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, id2JsonObject);
        return result;
    }

    @Override
    public Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {
        //检验参数是否合法
        deductGoodsInventories.forEach(d -> {
            if (d.getCount() <= 0) {
                throw new RuntimeException("purchase goods count need > 0");
            }
        });

        List<ECommerceGoods> eCommerceGoods = IterableUtils.toList(
                eCommerceGoodsDao.findAllById(deductGoodsInventories.stream()
                        .map(DeductGoodsInventory::getGoodsId).collect(Collectors.toList()))
        );
        //根据传递的goodsIds 查询不到商品对象则抛异常
        if (CollectionUtils.isEmpty(eCommerceGoods)) {
            throw new RuntimeException("can not found any goods by request");
        }

        //查询出来的商品数量与传递的不一致，抛异常
        if (eCommerceGoods.size() != deductGoodsInventories.size()) {
            throw new RuntimeException("request is not valid");
        }

        //goodsIf -> DeductGoodsInventory
        Map<Long, DeductGoodsInventory> goodsId2Inventory = deductGoodsInventories.stream()
                .collect(Collectors.toMap(DeductGoodsInventory::getGoodsId, Function.identity()));

        eCommerceGoods.forEach(g -> {
            Long currentInventory = g.getInventory();
            Integer needDeductInventory = goodsId2Inventory.get(g.getId()).getCount();
            if (currentInventory < needDeductInventory) {
                log.error(" goods inventory is not enough : [{}], [{}]",
                        currentInventory, needDeductInventory);
                throw new RuntimeException("goods inventory is not enough " + g.getId());
            }
            //扣减库存
            g.setInventory(currentInventory - needDeductInventory);
            log.info("deduct goods inventory : [{}],[{}],[{}] ", g.getId(), currentInventory, g.getInventory());
        });

        eCommerceGoodsDao.saveAll(eCommerceGoods);
        log.info("deduct goods inventory done");

        return true;
    }
}
