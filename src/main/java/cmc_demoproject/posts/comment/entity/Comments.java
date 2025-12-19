package cmc_demoproject.posts.comment.entity;

import cmc_demoproject.posts.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import cmc_demoproject.posts.post.entity.Posts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comments parent; // 부모 댓글

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comments> children = new ArrayList<>(); // 자식 댓글(대댓글)

    public void setUsers(Users users){
        if (this.users != null) {
            this.users.getComments().remove(this);
        }
        this.users = users;
        if (users != null && !users.getComments().contains(this)) {
            users.getComments().add(this);
        }
    }

    public void setPosts(Posts posts){
        if (this.posts != null) {
            this.posts.getComments().remove(this);
        }
        this.posts = posts;
        if (posts != null && !posts.getComments().contains(this)) {
            posts.getComments().add(this);
        }
    }
}
