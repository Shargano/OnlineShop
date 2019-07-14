package net.thumbtack.onlineshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.thumbtack.onlineshop.model.Category;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    private int id;
    private String name;
    private Integer parentId;
    private String parentName;

    public CategoryResponse() {
    }

    public CategoryResponse(int id, String name, Category parent) {
        this.id = id;
        this.name = name;
        if (parent != null) {
            parentName = parent.getName();
            parentId = parent.getId();
        }
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryResponse)) return false;
        CategoryResponse that = (CategoryResponse) o;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getParentId(), that.getParentId()) &&
                Objects.equals(getParentName(), that.getParentName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getParentId(), getParentName());
    }
}