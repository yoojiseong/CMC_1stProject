package cmc_demoproject.posts.post.service;

import cmc_demoproject.posts.post.dto.CategoryResponseDTO;
import cmc_demoproject.posts.post.entity.Categories;
import cmc_demoproject.posts.post.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponseDTO(category.getCategoryId(),category.getCategoryName()))
                .collect(Collectors.toList());
    }

    @Override
    public void addCategory(String category_name) {
        if(!categoryRepository.existsByCategoryName(category_name)) {
            Categories category = Categories.builder()
                            .categoryName(category_name)
                                    .build();

            categoryRepository.save(category);
        }
    }
}
