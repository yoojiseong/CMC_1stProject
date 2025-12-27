package cmc_demoproject.posts.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CategoryRequestDTO {
    private String category_name;
}
