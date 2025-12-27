package cmc_demoproject.posts.post.dto;

import cmc_demoproject.posts.comment.dto.CommentResponseDTO;
import cmc_demoproject.posts.user.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PostResponseDTO {
    private Long post_id;
    private String title;
    private String category;
    private String content;
    private boolean isBookmarked;
    private UserResponseDTO writer;
    private List<CommentResponseDTO> comment;
}
