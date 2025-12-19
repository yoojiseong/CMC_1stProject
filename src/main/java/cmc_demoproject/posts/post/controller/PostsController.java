package cmc_demoproject.posts.post.controller;

import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    ){
        log.info("게시글 목록 조회중... 카테고리 : "+categoryName);
        List<PostResponseDTO> response = postService.getPostList(categoryName);
        if(response != null)
            log.info("게시글이 없습니다..");
        else
            log.info(categoryName + "게시글 조회 목록 : "+response);
        return ResponseEntity.ok(response);
    }

}
