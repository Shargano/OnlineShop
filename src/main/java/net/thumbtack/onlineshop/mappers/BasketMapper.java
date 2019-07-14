package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.BasketProductItem;
import net.thumbtack.onlineshop.model.Client;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface BasketMapper {
    @Select("SELECT * FROM `basket` WHERE client_id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "items", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.BasketProductItemMapper.getByBasket", fetchType = FetchType.EAGER))
    })
    Basket getByClient(Client client);

    @Update("UPDATE `basketItem` JOIN `basket` ON `basketItem`.basket_id = `basket`.id " +
            "SET count = #{item.count} " +
            "WHERE `basket`.client_id = #{client.id} AND `basketItem`.id = #{item.id}")
    void changeCount(@Param("client") Client client, @Param("item") BasketProductItem item);

    @Delete("DELETE FROM `basketItem` WHERE basket_id = #{client.basket.id} AND product_id = #{product.id}")
    void deleteProduct(@Param("client") Client client, @Param("product") Product product);
}