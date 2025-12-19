package cmc_demoproject.posts.user.entity;


import cmc_demoproject.posts.bookmark.entity.Bookmarks;
import cmc_demoproject.posts.comment.entity.Comments;
import cmc_demoproject.posts.post.entity.Posts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Builder.Default
    @OneToMany(mappedBy = "users" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Posts> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "users" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "users" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmarks> bookmarks = new ArrayList<>();

    @Column(nullable = false)
    private String role;

    private String email;
    private String password;
    private String userName;

    public void addPosts(Posts posts){
        posts.setUsers(this);
    }
    public void addComments(Comments comments){
        comments.setUsers(this);
    }
    public void addBookmarks(Bookmarks bookmarks){
        bookmarks.setUsers(this);
    }

}
