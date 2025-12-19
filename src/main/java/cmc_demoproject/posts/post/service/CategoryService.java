package cmc_demoproject.posts.post.service;


import cmc_demoproject.posts.post.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    public List<CategoryResponseDTO> getAllCategories();
    public void addCategory(String category_name);
}
