package cmc_demoproject.posts.post.repository;

import cmc_demoproject.posts.post.entity.Posts;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts, Long> {
    @EntityGraph(attributePaths = {"users","categories"})
    List<Posts> findByCategories_CategoryName(String categoryName);

    @EntityGraph(attributePaths = {"users","categories"})
    List<Posts> findAll();
}
