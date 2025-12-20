package cmc_demoproject.posts.Service.TestCoverage;

import cmc_demoproject.posts.post.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CategoryServiceUnitTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("중복된 이름의 카테고리는 추가되지 않는다")
    void addCategory_DuplicateCheck() {
        // Given
        String catName = "IT";
        categoryService.addCategory(catName);
        int initialSize = categoryService.getAllCategories().size();

        // When: 동일 이름 추가
        categoryService.addCategory(catName);

        // Then: 사이즈가 그대로인지 확인
        assertEquals(initialSize, categoryService.getAllCategories().size());
    }
}