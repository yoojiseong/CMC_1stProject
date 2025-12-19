package cmc_demoproject.posts.post.service;

import cmc_demoproject.posts.post.dto.PostResponseDTO;

import java.util.List;

public interface PostService {
    public List<PostResponseDTO> getPostList(String categoryName);
}
