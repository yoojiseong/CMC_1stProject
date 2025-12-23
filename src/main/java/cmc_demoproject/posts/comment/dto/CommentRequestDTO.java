package cmc_demoproject.posts.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentRequestDTO {
    String content;
    Long parent_id;
}
