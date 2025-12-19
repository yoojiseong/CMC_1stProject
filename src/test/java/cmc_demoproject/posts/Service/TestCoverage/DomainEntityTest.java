package cmc_demoproject.posts.Service.TestCoverage;

import cmc_demoproject.posts.post.entity.Categories;
import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.user.entity.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainEntityTest {

    @Test
    @DisplayName("게시글과 유저의 양방향 연관관계 설정 테스트")
    void postUserAssociationTest() {
        // given
        Users user = Users.builder().userName("홍길동").build();
        Posts post = Posts.builder().title("제목").build();

        // when
        post.setUsers(user); // 연관관계 편의 메서드 호출

        // then
        assertThat(post.getUsers()).isEqualTo(user);
        assertThat(user.getPosts()).contains(post); // 유저의 리스트에도 게시글이 추가되었는지 확인
    }
}