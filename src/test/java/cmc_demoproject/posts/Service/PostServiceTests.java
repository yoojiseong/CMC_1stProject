package cmc_demoproject.posts.Service;

import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.entity.Categories;
import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.post.repository.CategoryRepository;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.post.service.PostService;
import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class PostServiceTests {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void addPost(){
        Long a = 2L;
        Users users = usersRepository.findByUser_id(a);
        Categories category = categoryRepository.findByCategoryName("정치");
        log.info(users.getUserName()+ category.getCategoryName());
        Posts post = Posts.builder()
                .title("테스트용")
                .content("테스트용 입니다.")
                .build();
        post.setUsers(users);
        post.setCategories(category);
        postRepository.save(post);
    }
}
