package cmc_demoproject.posts.comment.service;

import cmc_demoproject.posts.comment.dto.CommentRequestDTO;
import cmc_demoproject.posts.common.security.CustomUserDetails;

public interface CommentService {
    public void addComment(Long postId ,CommentRequestDTO dto , CustomUserDetails userDetails);
    void editComment(Long commentId , CustomUserDetails userDetails , CommentRequestDTO dto);
}
