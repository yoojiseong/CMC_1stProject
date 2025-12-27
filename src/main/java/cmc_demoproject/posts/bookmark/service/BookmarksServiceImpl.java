package cmc_demoproject.posts.bookmark.service;

import cmc_demoproject.posts.bookmark.entity.Bookmarks;
import cmc_demoproject.posts.bookmark.repository.BookmarksRepository;
import cmc_demoproject.posts.common.security.CustomUserDetails;
import cmc_demoproject.posts.post.entity.Posts;
import cmc_demoproject.posts.post.repository.PostRepository;
import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class BookmarksServiceImpl implements BookmarksService{
    private final UsersRepository usersRepository;
    private final PostRepository postRepository;
    private final BookmarksRepository bookmarkRepository;
    @Override
    public String toggleBookmarks(Long postId, CustomUserDetails userDetails) {
        Users user = usersRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        Optional<Bookmarks> existingBookmark = bookmarkRepository.findByUsersAndPosts(user, post);

        if(existingBookmark.isPresent()){
            bookmarkRepository.delete(existingBookmark.get());
            return "북마크가 해제되었습니다";
        }else{
            Bookmarks bookmark = new Bookmarks();

            bookmark.setUsers(user);
            bookmark.setPosts(post);

            bookmarkRepository.save(bookmark);
            return "북마크가 추가되었습니다.";
        }
    }
}
