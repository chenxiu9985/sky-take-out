package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 26706
 */
@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    @Select("select id, openid, name, phone, sex, id_number, avatar, create_time" +
            " from sky_take_out.user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     *
     * @param user
     */
    void insert(User user);
}
