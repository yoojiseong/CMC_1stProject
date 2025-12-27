package cmc_demoproject.posts.Service;

import cmc_demoproject.posts.post.dto.CategoryRequestDTO;
import cmc_demoproject.posts.post.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class CategoryServiceTests {
    @Autowired
    private CategoryService categoryService;
    @Test
    public void testAddCategory(){
        log.info("카테고리 추가 테스팅 중");
        CategoryRequestDTO dto = CategoryRequestDTO.builder()
                .category_name("주식")
                .build();
        categoryService.addCategory(dto);
        log.info("카테고리 주식 추가 완료");
    }
    @Test
    public void testDeleteCategory() {
    }
}
