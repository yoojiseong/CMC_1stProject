package cmc_demoproject.posts.post.service;

import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.dto.PostRequestDTO;
import cmc_demoproject.posts.post.dto.PostResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PostService {
    public List<PostResponseDTO> getPostList(String categoryName);
    public void addPost(PostRequestDTO dto , CustomUserDetails userDetails);
    public PostResponseDTO detailPost(Long postId);
}
