package net.thumbtack.onlineshop.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class BasketItemRequest {
    @NotNull(message = "Id can't be null")
    private int id;

    @NotNull(message = "Name can't be null")
    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotNull(message = "Price can't be null")
    @Positive(message = "Price must be positive")
    private int price;

    @Positive(message = "Count must be positive")
    private Integer count = 1;

    public BasketItemRequest() {
    }

    public BasketItemRequest(int id, String name, int price, Integer count) {
        this.id = id;
        this.name = name;
        this.price = price;
        if (count != null)
            this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}