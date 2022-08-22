package com.bitcamp221.didabara.mapper;

import com.bitcamp221.didabara.model.CategoryEntity;
import com.bitcamp221.didabara.model.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {

    @Select("SELECT * FROM category " +
            "JOIN user " +
            "ON category.host_id = user.id " +
            "WHERE user.username = #{userId} " +
            "LIMIT #{currentPage},10")
    List<Map<UserEntity, CategoryEntity>> selectMyRoomAndPage(@Param("userId") String userId, @Param("currentPage") int currentPage);
}
