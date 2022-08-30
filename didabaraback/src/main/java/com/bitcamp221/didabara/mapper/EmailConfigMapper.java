package com.bitcamp221.didabara.mapper;

import com.bitcamp221.didabara.model.EmailConfigEntity;
import com.bitcamp221.didabara.model.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

@Mapper
public interface EmailConfigMapper {

    @Select("SELECT * FROM emailconfig WHERE " +
            "email=#{emailConfigEntity.id} AND auth_code=#{emailConfigEntity.authCode}")
    EmailConfigEntity selectEmailAndCode(@Param("emailConfigEntity") EmailConfigEntity emailConfigEntity);

    // 작성자 : 김남주
    // INSERT INTO emailconfig(id, auth_code) VALUES(id=1, auth_code='123312')
    // date 컬럼 디폴트값 없어서 직접 set CURRENT_TIMESTAMP
    @Insert("INSERT INTO emailconfig(id, auth_code,created_date,modified_date) VALUES(id=#{user.id} , auth_code=#{code}, " +
            "created_date=CURRENT_TIMESTAMP, modified_date=CURRENT_TIMESTAMP)")
    int saveUserIntoEmailconfig(@Param("user") UserEntity user, @Param("code") String code);

    @Update("UPDATE emailconfig SET auth_code = #{code} WHERE id=#{user.id}")
    int updateUserIntoEmailconfig(@Param("user") UserEntity user, @Param("code") String code);



}
