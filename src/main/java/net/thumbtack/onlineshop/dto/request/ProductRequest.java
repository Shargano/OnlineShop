package net.thumbtack.onlineshop.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class ProductRequest {
    @NotBlank(message = "Name can't be empty")
    @NotNull(message = "Name can'be null")
    private String name;

    @NotNull(message = "Price can'be null")
    @Positive(message = "Price must be positive")
    private int price;

    @PositiveOrZero(message = "Count must be positive or zero")
    private Integer count = 0;
    private List<Integer> categories;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price, Integer count, List<Integer> categories) {
        this.name = name;
        this.price = price;
        if (count != null)
            this.count = count;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }
}