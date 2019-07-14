package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.Basket;
import net.thumbtack.onlineshop.model.BasketProductItem;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface BasketProductItemMapper {
    @Insert("INSERT INTO `basketItem` (basket_id, product_id, count) " +
            "VALUES (#{basket.id}, #{item.product.id}, #{item.count})")
    @Options(useGeneratedKeys = true, keyProperty = "item.id")
    void insert(@Param("basket") Basket basket, @Param("item") BasketProductItem item);

    @Insert("<script>" +
            "INSERT INTO `basketItem` (basket_id, product_id, count) VALUES " +
            "<foreach item='item' collection='list' separator=','>" +
            "(#{basket.id}, #{item.product.id}, #{item.count})" +
            "</foreach>" +
            "</script>")
    void insertList(@Param("basket") Basket basket, @Param("list") List<BasketProductItem> items);

    @Select("SELECT * FROM `basketItem` WHERE basket_id = #{id}")
    @Results({
            @Result(property = "product", column = "product_id", javaType = Product.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getById", fetchType = FetchType.EAGER))
    })
    List<BasketProductItem> getByBasket(Basket basket);

    @Delete("DELETE FROM `basketItem` WHERE basket_id = #{id}")
    void deleteAll(Basket basket);
}