package org.example.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.common.TableId;
import org.example.goods.DeductGoodsInventory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhoudashuai
 * @date 2022年04月07日 11:24 下午
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GoodsServiceTest {

    @Autowired
    private IGoodsService iGoodsService;

    @Test
    public void testGetGoodsInfoByTableId(){
        List<Long> ids = Arrays.asList(1L,2L,3L);
        List<TableId.Id> tIds = ids.stream().map(TableId.Id::new).collect(Collectors.toList());
        log.info("test get goods info by table id: [{}]", JSON.toJSONString(iGoodsService
        .getGoodsInfoByTableId(new TableId(tIds))));
    }

    @Test
    public void testGetSimpleGoodsInfoByPage(){
        log.info("test get simple goods info by page: [{}]",JSON.toJSONString(
                iGoodsService.getSimpleGoodsInfoByPage(1)
        ));
    }

    @Test
    public void testGetSimpleGoodsInfoByTableId(){
        List<Long> ids = Arrays.asList(1L,2L,3L);
        List<TableId.Id> tIds = ids.stream().map(TableId.Id::new).collect(Collectors.toList());
        log.info("test get simple goods info by table id: [{}]", JSON.toJSONString(iGoodsService
                .getSimpleGoodsInfoByTableId(new TableId(tIds))));
    }

    @Test
    public void testDeductGoodsInventory(){
        List<DeductGoodsInventory> deductGoodsInventories = Arrays.asList(
                new DeductGoodsInventory(1L,100),
                new DeductGoodsInventory(2L,66)
        );
        log.info("test deduct goods inventory: [{}]",iGoodsService.deductGoodsInventory(deductGoodsInventories));
    }
}
