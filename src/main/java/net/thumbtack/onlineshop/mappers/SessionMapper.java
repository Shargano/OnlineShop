package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.Session;
import net.thumbtack.onlineshop.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

public interface SessionMapper {
    @Insert("INSERT INTO `session` (id, user_id) " +
            "VALUES (#{id}, #{user.id})")
    void insert(Session session);

    @Select("SELECT * FROM `session` WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.UserMapper.getById", fetchType = FetchType.EAGER))
    })
    Session getById(String id);

    @Select("SELECT * FROM `session` WHERE user_id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.UserMapper.getById", fetchType = FetchType.LAZY))
    })
    Session getByUser(int id);

    @Delete("DELETE FROM `session` WHERE id = #{id}")
    void delete(Session session);
}