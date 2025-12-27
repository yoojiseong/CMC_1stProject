package cmc_demoproject.posts.post.service;

import cmc_demoproject.posts.comment.dto.CommentResponseDTO;
import cmc_demoproject.posts.comment.entity.Comments;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        List<CommentResponseDTO> commentDTOs = convertToHierarchy(post.getComments());
        return PostResponseDTO.builder()
                .post_id(post.getPostId())
                .title(post.getTitle())
                .category(post.getCategories().getCategoryName())
                .content(post.getContent())
                .writer(UserResponseDTO.builder()
                        .userName(post.getUsers().getUserName())
                        .userId(post.getUsers().getUserId())
                        .build())
                .comment(commentDTOs)
                .build();
    }

    @Override
    public void editPost(Long postId, CustomUserDetails userDetails, PostRequestDTO dto) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if(!post.getUsers().getUserId().equals(userDetails.getUserId())){
            throw new AccessDeniedException("작성자만 수정할 수 있습니다");
        }
        if(post.getCategories().getCategoryName() != dto.getCategory())
        {
            Categories newCategory;
            log.info("기존 게시글 카테고리 : "+post.getCategories().getCategoryName());
            log.info("바뀐 게시글 카테고리 : " + dto.getCategory());
            if(!categoryRepository.existsByCategoryName(dto.getCategory())) {
                log.info(dto.getCategory() + "이(가) DB에 없습니다 새로 생성중...");
                newCategory = Categories.builder()
                        .categoryName(dto.getCategory())
                        .build();
                categoryRepository.save(newCategory);
                log.info(dto.getCategory() + "이(가) DB에 저장되었습니다.");
            }
            log.info("게시글 수정중..");
            newCategory = categoryRepository.findByCategoryName(dto.getCategory());
            post.change(dto.getTitle(), dto.getContent());
            post.setCategories(newCategory);
        }
        else {
            post.change(dto.getTitle(), dto.getContent());
        }
    }

    @Override
    public void removePost(Long postId, CustomUserDetails userDetails) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if(!post.getUsers().getUserId().equals(userDetails.getUserId())){
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다");
        }
        postRepository.delete(post);
        log.info("게시글 " + post.getPostId() + "가 삭제되었습니다.");
    }

    @Override
    public List<PostResponseDTO> findAllPosts(CustomUserDetails userDetails) {
        List<Posts> posts = postRepository.findAll();
        Long currentUserId = (userDetails != null && userDetails.getUser() != null)
                ? userDetails.getUser().getUserId()
                : null;
        // 2. 엔티티 리스트를 DTO 리스트로 변환
        return posts.stream()
                .map(post -> {
                    // 작성자 정보 DTO 생성
                    UserResponseDTO writerDto = UserResponseDTO.builder()
                            .userName(post.getUsers().getUserName())
                            .userId(post.getUsers().getUserId())
                            .build();
                    boolean isBookmarked = false;
                    if (currentUserId != null &&  post.getBookmarks() != null) {
                        isBookmarked = post.getBookmarks().stream()
                                .filter(b -> b.getUsers() != null) // 북마크한 유저 정보가 있을 때만
                                .anyMatch(b -> b.getUsers().getUserId().equals(currentUserId));
                    }
                    log.info("북마크 : " +currentUserId+ isBookmarked);
                    // 최종 게시글 응답 DTO 생성
                    return PostResponseDTO.builder()
                            .post_id(post.getPostId())
                            .title(post.getTitle())
                            .category(post.getCategories() != null ? post.getCategories().getCategoryName() : "미분류")
                            .content(post.getContent())
                            .writer(writerDto)
                            .isBookmarked(isBookmarked)
                            .build();
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<CommentResponseDTO> convertToHierarchy(List<Comments> comments) {
        Map<Long, CommentResponseDTO> map = new HashMap<>();
        List<CommentResponseDTO> rootComments = new ArrayList<>();

        // 모든 댓글을 먼저 DTO로 변환하여 Map에 저장
        for (Comments comment : comments) {
            CommentResponseDTO dto = CommentResponseDTO.builder()
                    .commentId(comment.getCommentId())
                    .comment(comment.getContent())
                    .writer(comment.getUsers().getUserName())
                    .children(new ArrayList<>())
                    .build();
            map.put(dto.getCommentId(), dto);

            // 부모가 없으면 루트 리스트에 추가, 부모가 있으면 부모의 children에 추가
            if (comment.getParent() == null) {
                rootComments.add(dto);
            } else {
                CommentResponseDTO parentDto = map.get(comment.getParent().getCommentId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            }
        }
        return rootComments;
    }
}
