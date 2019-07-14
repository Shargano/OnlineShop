package net.thumbtack.onlineshop.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class PurchaseProductRequest {
    @NotNull(message = "Id can't be null")
    @PositiveOrZero(message = "Id must be positive or zero")
    private int id;

    @NotNull(message = "Name can'be null")
    @NotBlank(message = "Name can'be empty")
    private String name;

    @NotNull(message = "Price can'be null")
    @Positive(message = "Price must be positive")
    private int price;

    @Positive(message = "Count must be positive")
    private Integer count;

    public PurchaseProductRequest() {
        count = 1;
    }

    public PurchaseProductRequest(int id, String name, int price, Integer count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = Id;
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