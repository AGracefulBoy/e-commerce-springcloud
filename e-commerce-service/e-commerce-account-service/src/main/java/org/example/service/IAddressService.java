package org.example.service;

import org.example.account.AddressInfo;
import org.example.common.TableId;

/**
 * 用户地址相关服务接口定义
 * @author zhoudashuai
 * @date 2021年12月22日 10:46 下午
 */
public interface IAddressService {
    /**
     * 创建用户地址信息
     * @param addressInfo
     * @return
     */
    TableId createAddressInfo(AddressInfo addressInfo);

    /**
     * 获取当前用户登录的用户地址信息
     * @return
     */
    AddressInfo getCurrentAddressInfo();

    /**
     * 通过id获取用户地址信息，id是表的主键
     * @param id
     * @return
     */
    AddressInfo getAddressInfoById(Long id);

    /**
     * 通过tableId获取用户地址信息
     * @param tableId
     * @return
     */
    AddressInfo getAddressInfoByTableId(TableId tableId);
}
