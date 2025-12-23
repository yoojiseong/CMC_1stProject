package cmc_demoproject.posts.Service;

import cmc_demoproject.posts.comment.dto.CommentRequestDTO;
import cmc_demoproject.posts.comment.service.CommentService;
import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.repository.CategoryRepository;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.post.service.PostService;
import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.stream.events.Comment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Log4j2
public class CommentServiceTests {
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private CommentService commentService;
    @Test
    @DisplayName("일반 댓글 등록 테스트")
    public void addCommentTest() {
        // 1. 테스트 사용자 준비 (기존 DB에 있는 2번 유저 활용)
        Users user = userRepository.findById(2L)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 2. 댓글 요청 DTO 작성 (12번 게시물에 댓글 작성)
        CommentRequestDTO dto = CommentRequestDTO.builder()
                .content("테스트 댓글입니다.")
                .parent_id(null) // 일반 댓글
                .build();

        // 3. 서비스 호출 및 로그 확인
        commentService.addComment(12L, dto, userDetails);
        log.info("일반 댓글 등록 완료: " + dto.getContent());
    }
    @Test
    @DisplayName("대댓글 등록 테스트")
    public void addChildCommentTest() {
        // 1. 사용자 준비
        Users user = userRepository.findById(2L).orElseThrow();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 2. 대댓글 DTO 작성 (부모 댓글 ID가 1번이라고 가정)
        CommentRequestDTO childDto = CommentRequestDTO.builder()
                .content("ㄴ 대댓글 테스트입니다.")
                .parent_id(1L) // 실제 DB에 존재하는 부모 댓글 ID로 설정 필요
                .build();

        // 3. 서비스 호출
        try {
            commentService.addComment(12L, childDto, userDetails);
            log.info("대댓글 등록 완료: " + childDto.getContent());
        } catch (EntityNotFoundException e) {
            log.error("부모 댓글이 존재하지 않아 실패: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 작성 시 예외 발생 테스트")
    public void addCommentFailTest() {
        Users user = userRepository.findById(2L).orElseThrow();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 존재하지 않는 9999번 게시물 ID 설정
        CommentRequestDTO dto = CommentRequestDTO.builder()
                .content("실패 테스트")
                .build();

        assertThrows(EntityNotFoundException.class, () -> {
            commentService.addComment(9999L, dto, userDetails);
        });
        log.info("존재하지 않는 게시글 예외 처리 확인 완료");
    }
    @Test
    public void editCommentTest(){
        Users user = userRepository.findById(2L).orElseThrow();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        CommentRequestDTO dto = CommentRequestDTO.builder()
                .content("대댓글 수정 테스트 입니다.")
                .parent_id(3L)
                .build();
        commentService.editComment(4L, userDetails, dto);
        log.info("게시글 댓글 수정 완료");
    }
}
