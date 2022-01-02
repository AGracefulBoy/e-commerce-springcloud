package org.example.service.impl;

import org.example.filter.AccessContext;
import lombok.extern.slf4j.Slf4j;
import org.example.account.BalanceInfo;
import org.example.dao.EcommerceBalanceDao;
import org.example.entity.EcommerceBalance;
import org.example.service.IBalanceService;
import org.example.vo.LoginUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhoudashuai
 * @date 2021年12月25日 9:57 下午
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BalanceServiceImpl implements IBalanceService {

    private final EcommerceBalanceDao balanceDao;

    public BalanceServiceImpl(EcommerceBalanceDao balanceDao) {
        this.balanceDao = balanceDao;
    }

    @Override
    public BalanceInfo getCurrentUserBalanceInfo() {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        BalanceInfo balanceInfo =new BalanceInfo(
                loginUserInfo.getId(), 0L
        );

        EcommerceBalance ecommerceBalance =
                balanceDao.findByUserId(loginUserInfo.getId());

        if (null != ecommerceBalance){
            balanceInfo.setBalance(ecommerceBalance.getBalance());
        }else {
            // 如果还没有用户余额记录，这里创建出来，余额设定为0
            EcommerceBalance newBalance= new EcommerceBalance();
            newBalance.setUserId(loginUserInfo.getId());
            newBalance.setBalance(0L);
            log.info("init user balance record: [{}]",
                    balanceDao.save(newBalance).getId());
        }

        return balanceInfo;
    }

    @Override
    public BalanceInfo deductBalance(BalanceInfo balanceInfo) {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        // 扣减用户余额 需要先判断够不够
        EcommerceBalance ecommerceBalance=balanceDao.findByUserId(loginUserInfo.getId());
        if (null == ecommerceBalance
            || ecommerceBalance.getBalance() - balanceInfo.getBalance() < 0){
            throw new RuntimeException("user balance is not enough!");
        }

        Long sourceBalance = ecommerceBalance.getBalance();
        ecommerceBalance.setBalance(ecommerceBalance.getBalance()-balanceInfo.getBalance());

        log.info("deduct balance [{}],[{}],[{}]",
                balanceDao.save(ecommerceBalance).getId(),sourceBalance,
                balanceInfo.getBalance());

        return new BalanceInfo(
                ecommerceBalance.getUserId(),
                ecommerceBalance.getBalance()
        );
    }
}
