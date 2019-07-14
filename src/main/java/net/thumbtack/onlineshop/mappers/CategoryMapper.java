package net.thumbtack.onlineshop.mappers;

import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Set;

public interface CategoryMapper {
    @Insert({"<script>" +
            "INSERT INTO `category`(name<if test='parent != null'>, parentId</if>) " +
            "SELECT #{name}<if test='parent != null'>, id FROM `category` WHERE id = #{parent.id}</if>" +
            "</script>"})
    @Options(useGeneratedKeys = true)
    void insert(Category category);

    @Select("SELECT * FROM `category` WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "subcategories", column = "id", javaType = Set.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getSubcategories", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getByCategory", fetchType = FetchType.LAZY))
    })
    Category getById(int id);

    @Select("SELECT * FROM `category` WHERE parentId = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "subcategories", column = "id", javaType = Set.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getSubcategories", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getByCategory", fetchType = FetchType.LAZY))
    })
    Set<Category> getSubcategories(Category category);

    @Select("<script>" +
            "SELECT * FROM `category` WHERE id IN (" +
            "<foreach item='item' collection='list' separator=', '>" +
            "#{item}" +
            "</foreach>)" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "subcategories", column = "id", javaType = Set.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getSubcategories", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getByCategory", fetchType = FetchType.LAZY))
    })
    List<Category> getByListOfId(@Param("list") List<Integer> idList);

    @Select("SELECT category.id, category.name, category.parentId " +
            "FROM onlineshop.`category` " +
            "LEFT JOIN onlineshop.`category` AS parent ON category.parentId = parent.id " +
            "ORDER BY CASE WHEN parent.id IS NULL THEN category.name ELSE parent.name END, " +
            "CASE WHEN parent.id IS NULL THEN NULL ELSE category.name END")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "subcategories", column = "id", javaType = Set.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getSubcategories", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getByCategory", fetchType = FetchType.LAZY))
    })
    List<Category> getAll();

    @Update({"<script>" +
            "UPDATE `category` SET name = #{name}" +
            "<if test='parent != null'>, parentId = #{parent.id}</if> " +
            "WHERE id = #{id}" +
            "</script>"})
    void update(Category category);

    @Delete("DELETE FROM `category` WHERE id = #{id}")
    void delete(Category category);

    @Delete("DELETE FROM `category`")
    void deleteAll();

    @Select("SELECT * FROM `category` WHERE id IN (SELECT category_id FROM `product_category` WHERE product_id = #{id})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "subcategories", column = "id", javaType = Set.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getSubcategories", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getByCategory", fetchType = FetchType.LAZY))
    })
    List<Category> getByProduct(Product product);

    @Select("SELECT * FROM `category` WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    one = @One(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getById", fetchType = FetchType.LAZY)),
            @Result(property = "subcategories", column = "id", javaType = Set.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.CategoryMapper.getSubcategories", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.mappers.ProductMapper.getByCategory", fetchType = FetchType.LAZY))
    })
    List<Category> getByIdAsList(int id); //Это надо для ProductMapper - для метода OrderByCategory
}