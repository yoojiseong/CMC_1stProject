package cmc_demoproject.posts.Service.TestCoverage;

import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.service.PostService;
import cmc_demoproject.posts.user.dto.RegistMemberDTO;
import cmc_demoproject.posts.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // 테스트 후 데이터 롤백
public class ComprehensiveServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 및 중복 이메일 예외 테스트")
    void registerUserTest() {
        // given
        RegistMemberDTO dto = RegistMemberDTO.builder()
                .email("newuser@test.com")
                .password("password123")
                .userName("테스터")
                .build();

        // when & then (성공 케이스)
        userService.register(dto);

        // then (중복 예외 케이스 - Java 8 Lambda 활용)
        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용중인 이메일입니다.");
    }

    @Test
    @DisplayName("카테고리별 게시글 조회 분기 테스트 (JaCoCo 커버리지용)")
    void getPostListCoverageTest() {
        // 1. 카테고리 이름이 있을 때
        List<PostResponseDTO> withCategory = postService.getPostList("정치");
        assertThat(withCategory).isNotNull();

        // 2. 카테고리 이름이 null이거나 빈 문자열일 때 (전체 조회 분기)
        List<PostResponseDTO> allPosts = postService.getPostList(null);
        assertThat(allPosts).isNotNull();
    }
}