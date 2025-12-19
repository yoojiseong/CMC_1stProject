package cmc_demoproject.posts.post.repository;

import cmc_demoproject.posts.post.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    boolean existsByCategoryName(String category_name);
    Categories findByCategoryName(String categoryName);
}
