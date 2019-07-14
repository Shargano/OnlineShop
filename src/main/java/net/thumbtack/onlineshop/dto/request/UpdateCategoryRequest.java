package net.thumbtack.onlineshop.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.validation.constraints.NotBlank;

public class UpdateCategoryRequest {
    public static final Integer DEFAULT_PARENT = -1;

    @NotBlank(message = "Name can't be empty")
    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "parentId")
    private Integer parentId = DEFAULT_PARENT;

    public UpdateCategoryRequest() {
    }

    public UpdateCategoryRequest(String name, Integer parentId) {
        setName(name);
        setParentId(parentId);
    }

    public UpdateCategoryRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @JsonSetter
    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    @JsonSetter
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}