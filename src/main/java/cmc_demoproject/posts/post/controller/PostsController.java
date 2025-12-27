package cmc_demoproject.posts.post.controller;

import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.dto.PostRequestDTO;
import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.service.PostService;
import cmc_demoproject.posts.user.dto.RegistMemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Log4j2
public class PostsController {
    private final PostService postService;
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPostsInCategory(
            @RequestParam(value = "categoryName", required = false) String categoryName
            ,@AuthenticationPrincipal CustomUserDetails userDetails
    ){
        log.info("게시글 목록 조회중... 카테고리 : "+categoryName);
        List<PostResponseDTO> response = postService.getPostList(categoryName);
        if(response != null)
            log.info("게시글이 없습니다..");
        else
            log.info(categoryName + "게시글 조회 목록 : "+response);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> WritePosts(@AuthenticationPrincipal CustomUserDetails userDetails
            , @Valid @RequestBody PostRequestDTO dto){
        log.info("게시글 작성 중... : " + dto.getTitle() + dto.getContent());
        postService.addPost(dto , userDetails);
        return ResponseEntity.ok("게시글이 등록 되었습니다.");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> detailPost(@PathVariable("postId") Long postId){
        PostResponseDTO response = postService.detailPost(postId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{postId}")
    public ResponseEntity<String> detailPost(@PathVariable("postId") Long postId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails
                                                    , @Valid @RequestBody PostRequestDTO dto){
        try {
            postService.editPost(postId, userDetails, dto);
            return ResponseEntity.ok("게시글이 수정되었습니다.");
        }
        catch(AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> removePost(@PathVariable("postId") Long postId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails){
        try{
            postService.removePost(postId , userDetails);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        }
        catch(AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
