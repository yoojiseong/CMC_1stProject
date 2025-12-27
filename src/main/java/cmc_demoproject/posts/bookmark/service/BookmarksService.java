package cmc_demoproject.posts.bookmark.service;

import cmc_demoproject.posts.common.security.CustomUserDetails;

public interface BookmarksService {
    String toggleBookmarks(Long postId , CustomUserDetails userDetails);
}
