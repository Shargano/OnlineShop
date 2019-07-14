package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Insert({"<script>" +
            "INSERT INTO `user` (firstName, lastName<if test='#{patronymic} != null'>, patronymic</if>, login, password, userType) " +
            "VALUES (#{firstName}, #{lastName}<if test='#{patronymic} != null'>, #{patronymic}</if>, #{login}, #{password}, #{userType})" +
            "</script>"})
    @Options(useGeneratedKeys = true)
    void insert(User user);

    @Select("SELECT * FROM `user` WHERE id = #{id}")
    User getById(int id);

    @Select("SELECT * FROM `user` WHERE login = #{login}")
    User getByLogin(String login);

    @Delete("DELETE FROM `user`")
    void deleteAll();
}