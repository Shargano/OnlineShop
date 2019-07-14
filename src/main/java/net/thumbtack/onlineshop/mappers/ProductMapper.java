package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ProductMapper {
    @Insert("INSERT INTO `product` (name, price, count) VALUES (#{name}, #{price}, #{count})")
    @Options(useGeneratedKeys = true)
    void insert(Product product);

    @Insert("<script>" +
            "INSERT INTO `product_category` (product_id, category_id) VALUES " +
            "<foreach item='item' collection='list' separator=','>" +
            "(#{product.id}, #{item.id})" +
            "</foreach>" +
            "</script>")
    void addProductToCategories(@Param("product") Product product, @Param("list") List<Category> categories);

    @Select("SELECT * FROM `product` WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getByProduct", fetchType = FetchType.LAZY))
    })
    Product getById(int id);

    @Update("UPDATE `product` SET name = #{name}, price = #{price}, count = #{count}, version = #{version} + 1 WHERE id = #{id} AND version = #{version}")
    int update(Product product);

    @Update("UPDATE `product` SET count = #{count}, version = #{version} + 1 " +
            "WHERE id = #{id} AND name = #{name} AND price = #{price}")
    int updateCount(Product product);

    @Delete("DELETE FROM `product` WHERE id = #{id} AND version = #{version}")
    void delete(Product product);

    @Delete("DELETE FROM `product`")
    void deleteAll();

    @Delete("DELETE FROM `product_category` WHERE product_id = #{id}")
    void deleteCategories(Product product);

    @Select("SELECT * FROM `product` WHERE id IN (SELECT product_id FROM `product_category` WHERE category_id = #{category.id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getByProduct", fetchType = FetchType.LAZY))
    })
    List<Product> getByCategory(Category category);

    @Select("<script>" +
            "SELECT p.id AS id, p.name, p.price, p.count, p.version FROM `product_category` " +
            "RIGHT OUTER JOIN `product` p ON `product_category`.product_id = p.id " +
            "LEFT OUTER JOIN `category` c ON `product_category`.category_id = c.id " +
            "<choose>" +
            "<when test = 'list == null'></when>" +
            "<when test = 'list.size == 0'>WHERE c.id IS NULL </when>" +
            "<otherwise>WHERE c.id IN (" +
            "<foreach item = 'item' collection='list' separator=','>" +
            "#{item}" +
            "</foreach>) " +
            "</otherwise>" +
            "</choose>" +
            "GROUP BY (p.id) " +
            "ORDER BY p.name" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getByProduct", fetchType = FetchType.LAZY))
    })
    List<Product> getAllOrderByProduct(@Param("list") List<Integer> categories);

    @Select("<script>" +
            "SELECT p.id, p.name, p.price, p.count, p.version, c.id AS cid FROM `product_category` " +
            "RIGHT OUTER JOIN `product` p ON `product_category`.product_id = p.id " +
            "LEFT OUTER JOIN `category` c ON `product_category`.category_id = c.id " +
            "<choose>" +
            "<when test = 'list == null'></when>" +
            "<when test = 'list.size == 0'>WHERE c.id IS NULL </when>" +
            "<otherwise>WHERE c.id IN (" +
            "<foreach item = 'item' collection='list' separator=','>" +
            "#{item}" +
            "</foreach>) " +
            "</otherwise>" +
            "</choose>" +
            "ORDER BY c.name, p.name" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "cid", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getByIdAsList", fetchType = FetchType.LAZY))
    })
    List<Product> getAllOrderByCategory(List<Integer> categories);
}