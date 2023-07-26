package com.demo.dataservice.service;

import com.demo.api.model.FinanceAccount;
import com.demo.api.model.User;
import com.demo.api.pojo.UserAccountInfo;
import com.demo.api.service.UserService;
import com.demo.dataservice.mapper.FinanceAccountMapper;
import com.demo.dataservice.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class, version = "1.0")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Value("${ylb.config.password-salt}")
    private String passwordSalt;

    /**
     * 根据手机号查询数据
     *
     * @param phone
     * @return
     */
    @Override
    public User queryByPhone(String phone) {
        User user = null;
        if (CommonUtil.checkPhone(phone)) {
            user = userMapper.selectByPhone(phone);
        }
        return user;
    }

    /**
     * 用户注册
     *
     * @param phone
     * @param password
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int userRegister(String phone, String password) {
        int res = 0;
        if (CommonUtil.checkPhone(phone) && password != null && password.length() == 32) {

            //判断手机号在库中是否存在
            User queryUser = userMapper.selectByPhone(phone);
            if (queryUser == null) {
                //注册密码的md5二次加密
                String newPassword = DigestUtils.md5Hex(password + passwordSalt);

                //注册u_user
                User user = new User();
                user.setPhone(phone);
                user.setLoginPassword(newPassword);
                user.setAddTime(new Date());
                userMapper.insertReturnPrimaryKey(user);

                //获取主键user.getId()
                FinanceAccount account = new FinanceAccount();
                account.setUid(user.getId());
                account.setAvailableMoney(new BigDecimal("0"));
                financeAccountMapper.insertSelective(account);

                //成功
                res = 1;
            } else {
                //手机号存在
                res = 2;
            }
        }
        return res;
    }

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User userLogin(String phone, String password) {
        User user = null;
        String newPassword = DigestUtils.md5Hex(password + passwordSalt);
        user = userMapper.selectLogin(phone, newPassword);
        //更新最后的登录时间
        if (user != null) {
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
        }
        return user;
    }

    /**
     * 更新实名认证信息
     *
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    @Override
    public boolean modifyRealname(String phone, String name, String idCard) {
        int rows = userMapper.updateRealname(phone, name, idCard);
        return rows > 0;
    }

    /**
     * 获取用户和资金信息
     *
     * @param uid
     * @return
     */
    @Override
    public UserAccountInfo queryUserAllInfo(Integer uid) {
        UserAccountInfo info = null;
        if (uid != null && uid > 0) {
            info = userMapper.selectUserAccountById(uid);
        }
        return info;
    }
}
