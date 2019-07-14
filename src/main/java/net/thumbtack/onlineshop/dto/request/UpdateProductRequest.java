package net.thumbtack.onlineshop.dto.request;

import net.thumbtack.onlineshop.validator.NullOrNotBlank;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class UpdateProductRequest {
    @NullOrNotBlank
    private String name;

    @Positive(message = "Price must be positive")
    private Integer price;

    @PositiveOrZero(message = "Count must be positive or zero")
    private Integer count;
    private List<Integer> categories;

    public UpdateProductRequest() {
    }

    public UpdateProductRequest(String name, Integer price, Integer count, List<Integer> categories) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
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
