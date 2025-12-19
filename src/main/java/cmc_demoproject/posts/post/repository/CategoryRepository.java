package cmc_demoproject.posts.post.repository;

import cmc_demoproject.posts.post.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    boolean existsByCategoryName(String category_name);
}
