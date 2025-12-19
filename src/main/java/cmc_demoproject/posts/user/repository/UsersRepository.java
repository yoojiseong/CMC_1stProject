package cmc_demoproject.posts.user.repository;

import cmc_demoproject.posts.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query("select b from Users b where b.email = :email")
    Optional<Users> findByEmail(String user_id);

    boolean existsByMemberId(String mail);
}
