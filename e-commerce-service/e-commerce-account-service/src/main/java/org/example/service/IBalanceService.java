package org.example.service;

import org.example.account.BalanceInfo;

/**
 * 用户余额接口
 * @author zhoudashuai
 * @date 2021年12月22日 10:56 下午
 */
public interface IBalanceService {

    /**
     * 获取当前用户余额信息
     * @return
     */
    BalanceInfo getCurrentUserBalanceInfo();

    /**
     * 扣减用户余额
     * @param balanceInfo
     * @return
     */
    BalanceInfo deductBalance(BalanceInfo balanceInfo);
}
