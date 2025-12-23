package cmc_demoproject.posts.comment.service;

import cmc_demoproject.posts.comment.dto.CommentRequestDTO;
import cmc_demoproject.posts.comment.entity.Comments;
import cmc_demoproject.posts.comment.repository.CommentRepository;
import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CommentServiceImpl implements CommentService{

    private final PostRepository postRepository;
    private final CommentRepository commentsRepository;
    private final UsersRepository usersRepository;
    @Override
    public void addComment(Long postId , CommentRequestDTO dto , CustomUserDetails userDetails) {
        Users user = usersRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일을 가진 사용자를 찾지 못했습니다." + userDetails.getEmail()));
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(postId + "번 게시물이 존재하지 않습니다."));

        Comments comment = Comments.builder()
                .content(dto.getContent())
                .build();
        if(dto.getParent_id() != null) {
            Comments parentComment = commentsRepository.findByCommentId(dto.getParent_id())
                    .orElseThrow(() -> new EntityNotFoundException("부모 댓글을 찾을 수 없습니다."));
            if (!parentComment.getPosts().getPostId().equals(postId)) {
                throw new IllegalArgumentException("부모 댓글과 게시글 정보가 일치하지 않습니다.");
            }
            comment.setParent(parentComment);
        }
        comment.setUsers(user);
        comment.setPosts(post);
        commentsRepository.save(comment);
    }

    @Override
    public void editComment(Long commentId, CustomUserDetails userDetails, CommentRequestDTO dto) {
        Users user = usersRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일을 가진 사용자를 찾지 못했습니다." + userDetails.getEmail()));
        Comments parentComment = commentsRepository.findByCommentId(dto.getParent_id())
                .orElseThrow(() -> new EntityNotFoundException("상위 댓글을 찾을 수 없습니다."));
        Comments comment = commentsRepository.findByCommentId(commentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다" + commentId));
        if(parentComment==null)
            throw new AccessDeniedException("상위 댓글이 삭제되었거나 찾을 수 엇습니다.");
        if (!comment.getUsers().getUserId().equals(userDetails.getUserId())) {
            throw new AccessDeniedException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
        // 4. 내용 업데이트 (변경 감지 활용)
        if (dto.getContent() != null && !dto.getContent().isBlank()) {
            comment.change(dto.getContent());
        } else {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }
    }

}
