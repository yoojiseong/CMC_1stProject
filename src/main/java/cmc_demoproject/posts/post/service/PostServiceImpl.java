package cmc_demoproject.posts.post.service;

import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    @Override
    public List<PostResponseDTO> getPostList(String categoryName) {
        log.info("카테고리별 게시물 목록 조회중 ..." + categoryName);
        List<Posts> posts;
        if(categoryName != null && !categoryName.isBlank()){
            log.info(categoryName + "을(를) 찾았습니다!");
            posts = postRepository.findByCategoryName(categoryName);
        }
        else{
            log.info(categoryName + "을(를) 찾지 못했습니다. 전체 조회중...");
            posts = postRepository.findAll();
        }
        return posts.stream()
                .map(post -> PostResponseDTO.builder()
                        .post_id(post.getPost_id())
                        .title(post.getTitle())
                        .category(post.getCategories().getCategoryName())
                        .content(post.getContent())
                        .writer(UserResponseDTO.builder()
                                .userId(post.getUsers().getUser_id())
                                .userName(post.getUsers().getUserName())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
}
