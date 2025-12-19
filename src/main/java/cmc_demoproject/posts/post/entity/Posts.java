package cmc_demoproject.posts.post.entity;

import cmc_demoproject.posts.bookmark.entity.Bookmarks;
import cmc_demoproject.posts.comment.entity.Comments;
import cmc_demoproject.posts.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Categories categories;

    @OneToMany(mappedBy = "posts" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "posts" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmarks> bookmarks = new ArrayList<>();

    private String title;
    private String content;

    public void setUsers(Users users){
        if(this.users != null){
            this.users.getPosts().remove(this);
        }
        this.users = users;
        if(users != null && !users.getPosts().contains(this)){
            users.getPosts().add(this);
        }
    }
    public void setCategories(Categories categories){
        if(this.categories != null){
            this.categories.getPosts().remove(this);
        }
        this.categories = categories;
        if(categories != null && !categories.getPosts().contains(this)){
            categories.getPosts().add(this);
        }
    }

    public void addComments(Comments comments){
        this.comments.add(comments);
        comments.setPosts(this);
    }

    public void addBookmarks(Bookmarks bookmarks){
        this.bookmarks.add(bookmarks);
        bookmarks.setPosts(this);
    }
}
