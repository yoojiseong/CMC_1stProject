package cmc_demoproject.posts.post.entity;

import cmc_demoproject.posts.bookmark.entity.Bookmarks;
import cmc_demoproject.posts.comment.entity.Comments;
import cmc_demoproject.posts.user.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    @Builder.Default
    @OneToMany(mappedBy = "categories" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Posts> posts = new ArrayList<>();

    private String categoryName;

    public void addPosts(Posts posts){
        posts.setCategories(this);
    }

}
