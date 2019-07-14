package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Deposit;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ClientMapper {
    @Insert("INSERT INTO `client` (user_id, email, address, phone) " +
            "SELECT id, #{email}, #{address}, #{phone} FROM `user` WHERE login = #{login}")
    void insert(Client client);

    @Insert("INSERT INTO `deposit` (value, client_id) VALUES (0, #{id})")
    void createDeposit(Client client);

    @Insert("INSERT INTO `basket` (client_id) VALUES (#{id})")
    @Options(useGeneratedKeys = true, keyProperty = "basket.id")
    void createBasket(Client client);

    @Select("SELECT `user`.id, firstName, lastName, patronymic, email, address, phone, login, password, userType " +
            "FROM `client` " +
            "JOIN `user` ON `client`.user_id = `user`.id " +
            "WHERE user_id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "deposit", column = "id", javaType = Deposit.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ClientMapper.getDeposit", fetchType = FetchType.LAZY)),
            @Result(property = "basket", column = "id", javaType = Basket.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.BasketMapper.getByClient", fetchType = FetchType.LAZY))
    })
    Client getById(int id);

    @Select("SELECT `user`.id, firstName, lastName, patronymic, email, address, phone, login, password, userType " +
            "FROM `client` " +
            "JOIN `user` ON `client`.user_id = `user`.id ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "deposit", column = "id", javaType = Deposit.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ClientMapper.getDeposit", fetchType = FetchType.LAZY)),
            @Result(property = "basket", column = "id", javaType = Basket.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.BasketMapper.getByClient", fetchType = FetchType.LAZY))
    })
    List<Client> getAll();

    @Update("UPDATE `client` JOIN `user` ON `client`.user_id = `user`.id " +
            "SET firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic}, " +
            "email = #{email}, address = #{address}, phone = #{phone}, login = #{login}, password = #{password} " +
            "WHERE user_id = #{id}")
    void update(Client client);

    @Update("UPDATE `deposit` SET value = #{deposit.value}, version = #{deposit.version} + 1 " +
            "WHERE client_id = #{id} AND version = #{deposit.version}")
    int updateDeposit(Client client);

    @Select("SELECT value, version FROM `deposit` WHERE client_id = #{id}")
    Deposit getDeposit(Client client);

    @Delete("DELETE FROM `client` WHERE user_id = #{id}")
    void delete(Client client);

    @Delete("DELETE FROM `client`")
    void deleteAll();

    @Select("SELECT `user`.id, firstName, lastName, patronymic, email, address, phone, `deposit`.value, login, password " +
            "FROM `client` " +
            "JOIN `user` ON `client`.user_id = `user`.id " +
            "JOIN `deposit` ON `client`.user_id = `deposit`.client_id " +
            "WHERE `client`.id = #{client_id}")
    Client getByBasket(@Param("client_id") int id);
}