package cmc_demoproject.posts.bookmark.repository;

import cmc_demoproject.posts.bookmark.entity.Bookmarks;
import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarksRepository extends JpaRepository<Bookmarks, Long> {
    Optional<Bookmarks> findByBookmarkId(Long bookmarkId);
    Optional<Bookmarks> findByUsersAndPosts(Users user, Posts post);
    @Query("select b from Bookmarks b join fetch b.posts p where b.users = :user")
    List<Bookmarks> findAllByUsersWithFetchJoin(@Param("user") Users user);
}
