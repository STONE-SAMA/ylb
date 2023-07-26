package com.demo.dataservice.mapper;

import com.demo.api.model.User;
import com.demo.api.pojo.UserAccountInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    //统计注册人数
    int selectCountUser();

    //根据手机号查询用户
    User selectByPhone(@Param("phone") String phone);

    //添加记录,获取主键值
    int insertReturnPrimaryKey(User user);

    //登录
    User selectLogin(@Param("phone") String phone, @Param("loginPassword") String password);

    //查询用户信息
    UserAccountInfo selectUserAccountById(Integer uid);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updateRealname(@Param("phone") String phone, @Param("name") String name, @Param("idCard") String idCard);


}
