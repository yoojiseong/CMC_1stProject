package cmc_demoproject.posts.comment.repository;

import cmc_demoproject.posts.comment.entity.Comments;
import cmc_demoproject.posts.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    Optional<Comments> findByCommentId(Long commentId);
}
