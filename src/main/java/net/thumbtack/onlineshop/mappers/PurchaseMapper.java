package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface PurchaseMapper {
    @Insert("INSERT INTO `purchase` (client_id, product_id, count, productName, productPrice) " +
            "VALUES (#{client.id}, #{product.id}, #{count}, #{product.name}, #{product.price})")
    @Options(useGeneratedKeys = true)
    void insert(Purchase purchase);

    @Insert("<script>" +
            "INSERT INTO `purchase` (client_id, product_id, count, productName, productPrice) VALUES " +
            "<foreach item='item' collection='list' separator=', '>" +
            "(#{item.client.id}, #{item.product.id}, #{item.count}, #{item.product.name}, #{item.product.price})" +
            "</foreach>" +
            "</script>")
    void batchInsert(@Param("list") List<Purchase> purchases);

    @Select("SELECT * FROM `purchase` WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "client", column = "client_id", javaType = Client.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ClientMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "product", column = "product_id", javaType = Product.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getById", fetchType = FetchType.LAZY))
    })
    Purchase getById(int id);

    @Delete("DELETE FROM `purchase` WHERE id = #{id}")
    void delete(Purchase purchase);

    @Select("SELECT * FROM `purchase` WHERE client_id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "product", column = "product_id", javaType = Product.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getById", fetchType = FetchType.LAZY))
    })
    List<Purchase> getClientPurchases(Client client);

    @Select("<script>" +
            "SELECT `user`.id, firstName, lastName, patronymic, email, address, phone, login, password, userType " +
            "FROM `client` " +
            "JOIN `user` ON `client`.user_id = `user`.id " +
            "<if test='list != null'><if test='list.size > 0'>" +
            "WHERE client.user_id IN " +
            "(<foreach item='item' collection='list' separator=', '>" +
            "#{item}" +
            "</foreach>) " +
            "</if></if>" +
            "<if test='order != null'>ORDER BY #{order} </if>" +
            "<if test='limit != null'>LIMIT #{limit} </if>" +
            "<if test='offset != null'>OFFSET #{offset}</if>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "deposit", column = "id", javaType = Deposit.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ClientMapper.getDeposit", fetchType = FetchType.LAZY)),
            @Result(property = "basket", column = "id", javaType = Basket.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.BasketMapper.getByClient", fetchType = FetchType.LAZY)),
            @Result(property = "purchases", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.PurchaseMapper.getClientPurchases", fetchType = FetchType.LAZY))
    })
    List<Client> getAllByClients(@Param("list") List<Integer> clients,
                                 @Param("offset") Integer offset,
                                 @Param("limit") Integer limit,
                                 @Param("order") String order);

    @Select("<script>" +
            "SELECT * FROM `purchase` " +
            "<if test='list.size > 0'>" +
            "WHERE product_id IN " +
            "(<foreach item='item' collection='list' separator=', '>" +
            "#{item}" +
            "</foreach>) " +
            "</if>" +
            "<if test='order == null'>ORDER BY productName </if>" +
            "<if test='order != null'>ORDER BY #{order} </if>" +
            "<if test='limit != null'>LIMIT #{limit} </if>" +
            "<if test='offset != null'>OFFSET #{offset}</if>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "client", column = "client_id", javaType = Client.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ClientMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "product", column = "product_id", javaType = Product.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getById", fetchType = FetchType.LAZY))
    })
    List<Purchase> getAllByProducts(@Param("list") List<Integer> products,
                                    @Param("offset") Integer offset,
                                    @Param("limit") Integer limit,
                                    @Param("order") String order);

    @Select("<script>" +
            "SELECT purchase.id, purchase.product_id, productName, productPrice, purchase.count, client_id FROM `purchase` purchase " +
            "LEFT OUTER JOIN `product_category` pc ON purchase.product_id = pc.product_id " +
            "LEFT OUTER JOIN `category` child ON pc.category_id = child.id " +
            "LEFT OUTER JOIN `category` parent ON child.parentId = parent.id " +
            "<if test='list.size > 0'>" +
            "WHERE pc.category_id IN " +
            "(<foreach item='item' collection='list' separator=', '>" +
            "#{item}" +
            "</foreach>) " +
            "</if>" +
            "<if test='order == null'>ORDER BY child.name, purchase.productName, client_id </if>" +
            "<if test='order != null'>ORDER BY #{order} </if>" +
            "<if test='limit != null'>LIMIT #{limit} </if>" +
            "<if test='offset != null'>OFFSET #{offset}</if>" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "client", column = "client_id", javaType = Client.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ClientMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "product", column = "product_id", javaType = Product.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getById", fetchType = FetchType.LAZY))
    })
    List<Purchase> getAllByCategories(@Param("list") List<Integer> categories,
                                      @Param("offset") Integer offset,
                                      @Param("limit") Integer limit,
                                      @Param("order") String order);
}