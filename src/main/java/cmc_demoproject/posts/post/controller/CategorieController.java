package cmc_demoproject.posts.post.controller;

import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.dto.CategoryRequestDTO;
import cmc_demoproject.posts.post.dto.CategoryResponseDTO;
import cmc_demoproject.posts.post.entity.Categories;
import cmc_demoproject.posts.post.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Log4j2
public class CategorieController {
        private final CategoryService categoryService;

        @GetMapping
        public ResponseEntity<List<CategoryResponseDTO>> getCategories () {
            return ResponseEntity.ok(categoryService.getAllCategories());
        }
        @PostMapping
        public ResponseEntity<String> createCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @RequestBody CategoryRequestDTO dto){
                log.info("카테고리 추가중..." + dto.getCategory_name());
                categoryService.addCategory(dto);
                return ResponseEntity.ok("카테고리 추가가 완료되었습니다.");
        }
}
