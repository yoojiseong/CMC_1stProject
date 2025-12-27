package cmc_demoproject.posts.comment.controller;

import cmc_demoproject.posts.comment.dto.CommentRequestDTO;
import cmc_demoproject.posts.comment.service.CommentService;
import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.dto.PostRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<String> addComment(@PathVariable("postId") Long postId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails
            , @Valid @RequestBody CommentRequestDTO dto){
        try {
            commentService.addComment(postId , dto , userDetails);
            return ResponseEntity.ok("댓글이 등록되었습니다.");
        }catch (EntityNotFoundException e) {
            // 게시글이나 유저가 없을 경우 처리
            log.info("게시글 또는 유저가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<String> editComment(@PathVariable("commentId") Long commentId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails
            , @Valid @RequestBody CommentRequestDTO dto) {
        try {
            commentService.editComment(commentId , userDetails, dto );
            return ResponseEntity.ok("댓글이 수정되었습니다.");
        }catch (EntityNotFoundException e) {
            // 게시글이나 유저가 없을 경우 처리
            log.info("게시글 또는 유저가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable("commentId") Long commentId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails){
        try{
            commentService.removeComment(commentId , userDetails);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
