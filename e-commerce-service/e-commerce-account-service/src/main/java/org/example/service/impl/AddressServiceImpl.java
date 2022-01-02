package org.example.service.impl;

import com.alibaba.fastjson.JSON;
import org.example.filter.AccessContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.example.account.AddressInfo;
import org.example.common.TableId;
import org.example.dao.EcommerceAddressDao;
import org.example.entity.EcommerceAddress;
import org.example.service.IAddressService;
import org.example.vo.LoginUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhoudashuai
 * @date 2021年12月22日 11:00 下午
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AddressServiceImpl implements IAddressService {

    private final EcommerceAddressDao ecommerceAddressDao;

    public AddressServiceImpl(EcommerceAddressDao ecommerceAddressDao) {
        this.ecommerceAddressDao = ecommerceAddressDao;
    }

    /**
     * 存储多个地址信息
     * @param addressInfo
     * @return
     */
    @Override
    public TableId createAddressInfo(AddressInfo addressInfo) {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        //将传递的参数转换为实体对象
        List<EcommerceAddress> ecommerceAddresses =addressInfo.getAddressItems().stream()
                .map(a -> EcommerceAddress.to(loginUserInfo.getId(),a))
                .collect(Collectors.toList());

        // 保存到数据表并把返回记录的id给调用方
        List<EcommerceAddress> savedRecords = ecommerceAddressDao.saveAll(ecommerceAddresses);

        List<Long> ids = savedRecords.stream()
        .map(EcommerceAddress::getId).collect(Collectors.toList());
        log.info("create address info: [{}],[{}]",loginUserInfo.getId(),
                JSON.toJSONString(ids));

        return new TableId(
                ids.stream().map(TableId.Id::new).collect(Collectors.toList())
        );
    }

    @Override
    public AddressInfo getCurrentAddressInfo() {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        //根据userid 查询到用户的地址信息，再实现转化
        List<EcommerceAddress> ecommerceAddresses = ecommerceAddressDao.findAllByUserId(loginUserInfo.getId());

        List<AddressInfo.AddressItem> addressItems = ecommerceAddresses.stream()
                .map(EcommerceAddress::toAddressItem)
                .collect(Collectors.toList());

        return new AddressInfo(loginUserInfo.getId(),addressItems);
    }

    @Override
    public AddressInfo getAddressInfoById(Long id) {

        EcommerceAddress ecommerceAddress = ecommerceAddressDao.findById(id).orElse(null);

        if (null == ecommerceAddress){
            throw new RuntimeException("address is not exist");
        }

        return new AddressInfo(ecommerceAddress.getUserId(),
                Collections.singletonList(ecommerceAddress.toAddressItem()));
    }

    @Override
    public AddressInfo getAddressInfoByTableId(TableId tableId) {

        List<Long> ids  = tableId.getIds().stream()
                .map(TableId.Id::getId).collect(Collectors.toList());

        log.info("get address info by table id: [{}]",JSON.toJSONString(ids));

        List<EcommerceAddress> ecommerceAddresses = ecommerceAddressDao.findAllById(ids);

        if (CollectionUtils.isEmpty(ecommerceAddresses)){
            return new AddressInfo(-1L,Collections.emptyList());
        }

        List<AddressInfo.AddressItem> addressItems = ecommerceAddresses.stream()
                .map(EcommerceAddress::toAddressItem)
                .collect(Collectors.toList());

        return new AddressInfo(ecommerceAddresses.get(0).getUserId(),addressItems);
    }
}
