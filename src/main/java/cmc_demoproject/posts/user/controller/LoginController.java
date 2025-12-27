package cmc_demoproject.posts.user.controller;

import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.dto.CategoryResponseDTO;
import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.post.service.CategoryService;
import cmc_demoproject.posts.post.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
@Log4j2
public class LoginController {
    private final PostService postService;
    private final CategoryService categoryService;

    @GetMapping("/view/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/view/signup")
    public String signupPage() {
        return "signup";
    }
    @GetMapping("/view/board")
    public String boardPage(Model model ,@AuthenticationPrincipal CustomUserDetails userDetails){
        List<PostResponseDTO> posts = postService.findAllPosts(userDetails);
        model.addAttribute("posts", posts);
        for(PostResponseDTO post : posts) {
            log.info("북마크 확인 정보 : " + post.isBookmarked());
        }
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "board";
    }
    @GetMapping("/view/posts/{postId}")
    public String postDetailPage(@PathVariable Long postId, Model model) {
        // 게시글 상세 데이터 조회 (서비스 내에서 댓글 목록도 함께 가져오도록 구현되어 있어야 함)
        PostResponseDTO post = postService.detailPost(postId);
        model.addAttribute("post", post);

        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "post-detail"; // post-detail.html
    }
    @GetMapping("/view/posts/create")
    public String postCreatePage(Model model) {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "post-create"; // post-create.html 파일을 찾아갑니다.
    }
}
