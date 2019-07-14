package net.thumbtack.onlineshop.model;

import java.util.*;

public class Category {
    private int id;
    private String name;
    private Category parent;
    private Set<Category> subcategories;
    private List<Product> products;

    public Category() {
        subcategories = new HashSet<>();
        products = new ArrayList<>();
    }

    public Category(int id, String name, Category parent, Set<Category> subcategories, List<Product> products) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.subcategories = subcategories;
        this.products = products;
    }

    public Category(String name, Category parent) {
        this(0, name, parent, new HashSet<>(), new ArrayList<>());
    }

    public Category(String name) {
        this(0, name, null, new HashSet<>(), new ArrayList<>());
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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Set<Category> subcategories) {
        this.subcategories = subcategories;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return getId() == category.getId() &&
                Objects.equals(getName(), category.getName()) &&
                Objects.equals(getParent(), category.getParent()) &&
                Objects.equals(getSubcategories(), category.getSubcategories()) &&
                Objects.equals(getProducts(), category.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getParent(), getSubcategories(), getProducts());
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                ", subcategories=" + subcategories +
                ", products=" + products +
                '}';
    }
}