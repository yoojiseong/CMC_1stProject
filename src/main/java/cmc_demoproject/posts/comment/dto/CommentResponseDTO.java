package cmc_demoproject.posts.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private String comment;
    private String writer;
    private List<CommentResponseDTO> children;
}
