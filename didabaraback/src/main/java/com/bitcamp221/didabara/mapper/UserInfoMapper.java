package com.bitcamp221.didabara.mapper;

import com.bitcamp221.didabara.model.UserEntity;
import com.bitcamp221.didabara.model.UserInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.Map;

@Mapper
public interface UserInfoMapper {

    @Select("SELECT * FROM user WHERE id=#{id}")
    UserEntity findByIdInUser(@Param("id") String id);


    @Select("SELECT * FROM user_info WHERE id=#{id}")
    UserInfoEntity findByIdInUserInfo(@Param("id") String id);

    @Select("SELECT * FROM user_info JOIN user ON user_info.id = user.id WHERE user.id=#{id}")
    Map findByMap(@Param("id") String id);

    @Update("UPDATE user_info " +
            "JOIN user " +
            "ON user_info.id = user.id " +
            "SET user.nickname = #{map.nickname}, " +
            "user.password = #{map.password}, " +
            "user_info.job = #{map.job}, " +
            "user_info.profile_image_url = #{map.profile_image_url} " +
            "WHERE user.id = #{id} ")
    int updateUserInfo(@Param("id") String id, @Param("map") Map map);

    @Select("SELECT * FROM user WHERE nickname=#{map.nickname}")
    UserEntity checkNickname(@Param("map") Map map);

    @Delete("DELETE a,b FROM user_info a " +
            "LEFT JOIN user b " +
            "ON a.id = b.id " +
            "WHERE a.id = #{id}")
    int deleteUserAndInfo(@Param("id") String id);

}