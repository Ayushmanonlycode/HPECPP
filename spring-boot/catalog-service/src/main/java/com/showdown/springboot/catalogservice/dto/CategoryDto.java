package com.showdown.springboot.catalogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



public class CategoryDto {

    private String id;

    @NotBlank(message = "Category name is required")
    @Size(max = 255, message = "Category name must be at most 255 characters")
    private String name;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    public CategoryDto() {
    }

    public CategoryDto(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

