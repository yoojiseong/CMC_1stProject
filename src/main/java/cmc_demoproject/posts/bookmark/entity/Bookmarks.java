package cmc_demoproject.posts.bookmark.entity;


import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Bookmarks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmark_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Posts posts;

    public void setUsers(Users users){
        if (this.users != null) {
            this.users.getBookmarks().remove(this);
        }
        this.users = users;
        if (users != null && !users.getBookmarks().contains(this)) {
            users.getBookmarks().add(this);
        }
    }

    public void setPosts(Posts posts){
        if (this.posts != null) {
            this.posts.getBookmarks().remove(this);
        }
        this.posts = posts;
        if (posts != null && !posts.getBookmarks().contains(this)) {
            posts.getBookmarks().add(this);
        }
    }
}
