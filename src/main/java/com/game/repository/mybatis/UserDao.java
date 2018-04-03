package com.game.repository.mybatis;

import com.game.repository.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserDao {

    User queryByName(@Param("name") String name);

}

