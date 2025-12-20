package cmc_demoproject.posts.Service;

import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.dto.PostRequestDTO;
import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.entity.Categories;
import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.post.repository.CategoryRepository;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.post.service.PostService;
import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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
        Users user = usersRepository.findByEmail("test@naver.com") // findByUserId 대신 표준 findById 권장
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: "));
        log.info(user.getUserName());
        Categories category = categoryRepository.findByCategoryName("정치");
        log.info(category.getCategoryName());
        Posts post = Posts.builder()
                .title("테스트용")
                .content("테스트용 입니다.")
                .users(user)
                .categories(category)
                .build();
        log.info(post.getCategories().getCategoryName());
        postRepository.save(post);
    }
    @Test
    public void addPosts(){
        Long a = 1L;
        Users user = usersRepository.findByEmail("test2@naver.com") // findByUserId 대신 표준 findById 권장
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: "));
        log.info(user.getUserName());
        Categories category = categoryRepository.findByCategoryId(a);
        log.info(category.getCategoryName());
        for(int i=0; i<10; i++) {
            Posts post = Posts.builder()
                    .title("테스트용" + i)
                    .content("테스트용 입니다." + i)
                    .users(user)
                    .categories(category)
                    .build();
            log.info(post.getCategories().getCategoryName());
            postRepository.save(post);
        }
    }

    @Test
    public void findPostById(){
        List<Posts> posts = postRepository.findAll();
        for(Posts post : posts){
            log.info(post.getTitle());
        }
    }

    @Test
    public void findPost(){
        List<Posts> posts = postRepository.findByCategories_CategoryId(1L);
        for(Posts post : posts){
            log.info(post.getTitle());
        }
    }

    @Test
    public void detailPost(){
        PostResponseDTO response = postService.detailPost(12L);
        log.info("게시물 상세 조회 테스트" + response.getTitle() + response.getContent() + response.getWriter().getUserName());
    }

    @Test
    public void changePost(){
        Users user = usersRepository.findById(2L).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. _PostServiceTests"));
        CustomUserDetails userDetails = new CustomUserDetails(user);
        PostRequestDTO dto = PostRequestDTO.builder()
                .title("게시물 수정 테스트 _ 지식 In")
                .content("게시물 수정 테스트 코드 입니다. _ 지식 In")
                .category("지식In")
                .build();
        postService.editPost(12L , userDetails , dto);
        Posts post = postRepository.findById(12L).orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다._PostServiceTests"));
        log.info(post.getTitle() , post.getContent());
    }

    @Test
    public void removePost(){
        Users user = usersRepository.findById(2L).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. _PostServiceTests"));
        CustomUserDetails userDetails = new CustomUserDetails(user);
        postService.removePost(1L , userDetails);
    }
}
