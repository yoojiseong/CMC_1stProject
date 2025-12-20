package cmc_demoproject.posts.post.service;

import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.dto.PostRequestDTO;
import cmc_demoproject.posts.post.dto.PostResponseDTO;
import cmc_demoproject.posts.post.entity.Categories;
import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.post.repository.CategoryRepository;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.user.dto.UserResponseDTO;
import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UsersRepository usersRepository;

    @Override
    public List<PostResponseDTO> getPostList(String categoryName) {
        log.info("카테고리별 게시물 목록 조회중 ..." + categoryName);
        List<Posts> posts;
        if(categoryName != null && !categoryName.isBlank()){
            log.info(categoryName + "을(를) 찾았습니다!");
            posts = postRepository.findByCategories_CategoryName(categoryName);
        }
        else{
            log.info(categoryName + "을(를) 찾지 못했습니다. 전체 조회중...");
            posts = postRepository.findAll();
        }
        return posts.stream()
                .map(post -> PostResponseDTO.builder()
                        .post_id(post.getPostId())
                        .title(post.getTitle())
                        .category(post.getCategories().getCategoryName())
                        .content(post.getContent())
                        .writer(UserResponseDTO.builder()
                                .userId(post.getUsers().getUserId())
                                .userName(post.getUsers().getUserName())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void addPost(PostRequestDTO dto, CustomUserDetails userDetails) {
        if(!categoryRepository.existsByCategoryName(dto.getCategory())) {
            categoryRepository.save(Categories.builder()
                    .categoryName(dto.getCategory())
                    .build());
        }
        Categories category = categoryRepository.findByCategoryName(dto.getCategory());
        Users user = usersRepository.findByEmail(userDetails.getEmail())
                        .orElseThrow(() -> new EntityNotFoundException("해당 이메일을 가진 사용자를 찾지 못했습니다." + userDetails.getEmail()));
        log.info("게시물을 작성하는 작성자 : " + user.getUserName());
        log.info("게시글 카테고리 : " + category.getCategoryName());
        postRepository.save(Posts.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .users(user)
                .categories(category)
                .build());
    }

    @Override
    public PostResponseDTO detailPost(Long postId) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다"));
        return PostResponseDTO.builder()
                .post_id(post.getPostId())
                .title(post.getTitle())
                .category(post.getCategories().getCategoryName())
                .content(post.getContent())
                .writer(UserResponseDTO.builder()
                        .userName(post.getUsers().getUserName())
                        .userId(post.getUsers().getUserId())
                        .build())
                .build();
    }
}
