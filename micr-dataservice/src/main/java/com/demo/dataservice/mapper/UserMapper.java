package com.demo.dataservice.mapper;

import com.demo.api.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    //统计注册人数
    int selectCountUser();

    //根据手机号查询用户
    User selectByPhone(@Param("phone") String phone);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


}
