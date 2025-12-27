package cmc_demoproject.posts.bookmark.controller;

import cmc_demoproject.posts.bookmark.repository.BookmarksRepository;
import cmc_demoproject.posts.bookmark.service.BookmarksService;
import cmc_demoproject.posts.common.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarksService bookmarkService;
    @PostMapping
    public ResponseEntity<String> toggleBookmark(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        String message = bookmarkService.toggleBookmarks(postId, userDetails);
        return ResponseEntity.ok(message);
    }

}
