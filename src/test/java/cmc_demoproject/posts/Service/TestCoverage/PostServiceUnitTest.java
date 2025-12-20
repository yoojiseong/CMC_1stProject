package cmc_demoproject.posts.Service.TestCoverage;

import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.dto.PostRequestDTO;
import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.post.service.PostService;
import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Log4j2
public class PostServiceUnitTest {

    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;
    @Autowired
    private UsersRepository usersRepository;


    @Test
    @DisplayName("게시글 수정 시 작성자가 아니면 예외가 발생한다")
    void editPost_AccessDenied() {
        // Given: 다른 사용자의 정보 준비
        Users anotherUser = Users.builder().email("other@test.com").role("USER").build();
        usersRepository.save(anotherUser);
        CustomUserDetails otherDetails = new CustomUserDetails(anotherUser);

        // When & Then: 수정 시도 시 AccessDeniedException 발생 확인
        assertThrows(AccessDeniedException.class, () -> {
            postService.editPost(1L, otherDetails, new PostRequestDTO("제목", "내용", "정치"));
        });
    }

}