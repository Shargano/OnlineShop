package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.Administrator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AdminMapper {
    @Insert("INSERT INTO `admin` (user_id, position) " +
            "SELECT id, #{position} FROM `user` WHERE login = #{login}")
    void insert(Administrator admin);

    @Select("SELECT `user`.id, firstName, lastName, patronymic, position, login, password, userType FROM `admin` " +
            "JOIN `user` ON `admin`.user_id = `user`.id WHERE user_id = #{id}")
    Administrator getById(int adminID);

    @Update("UPDATE `admin` JOIN `user` ON `admin`.user_id = `user`.id " +
            "SET firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic}, " +
            "position = #{position}, login = #{login}, password = #{password} " +
            "WHERE user_id = #{id}")
    void update(Administrator administrator);

    @Delete("DELETE FROM `admin` WHERE user_id = #{id}")
    void delete(Administrator admin);

    @Delete("DELETE FROM `admin`")
    void deleteAll();
}