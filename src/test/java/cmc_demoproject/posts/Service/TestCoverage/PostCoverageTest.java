package cmc_demoproject.posts.Service.TestCoverage;

import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostCoverageTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("카테고리 이름이 있을 때 - 분기 1번 테스트")
    public void getPostListWithCategory() {
        // given: "정치" 카테고리가 데이터베이스에 있다고 가정 (DataInitializer 등 활용)
        String categoryName = "정치";

        // when
        List<PostResponseDTO> result = postService.getPostList(categoryName);

        // then: 리포트의 'if(categoryName != null...)' 블록이 녹색으로 채워짐
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("카테고리 이름이 null이거나 비어있을 때 - 분기 2번 테스트")
    public void getPostListEmpty() {
        // when: 카테고리명을 넘기지 않음
        List<PostResponseDTO> result = postService.getPostList(null);

        // then: 리포트의 'else' 블록(전체 조회)이 녹색으로 채워짐
        assertThat(result).isNotNull();
    }
}