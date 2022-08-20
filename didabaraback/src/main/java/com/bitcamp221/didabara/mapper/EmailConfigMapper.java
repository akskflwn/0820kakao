package com.bitcamp221.didabara.mapper;

import com.bitcamp221.didabara.model.EmailConfigEntity;
import com.bitcamp221.didabara.model.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface EmailConfigMapper {

    @Select("SELECT * FROM emailconfig WHERE " +
            "email=#{emailConfigEntity.email} AND auth_code=#{emailConfigEntity.authCode}")
    EmailConfigEntity selectEmailAndCode(@Param("emailConfigEntity") EmailConfigEntity emailConfigEntity);

    // 작성자 : 김남주
    // INSERT INTO emailconfig(id, auth_code) VALUES(id=1, auth_code='123312')
    // date 컬럼 디폴트값 없어서 직접 set CURRENT_TIMESTAMP
    @Insert("INSERT INTO emailconfig(id,created_date,modified_date,auth_code) VALUES(id=#{user.id} , " +
            "auth_code=#{code})")
    int saveUserIntoEmailconfig(@Param("user") UserEntity user, @Param("code") String code);


    @Select(
            "select emailconfig.auth_code from emailconfig \n" +
                    "join user\n" +
                    "on emailconfig.id=user.id\n" +
                    "where user.username=#{username}")
    Map findCode(@Param("username")String username);

}
