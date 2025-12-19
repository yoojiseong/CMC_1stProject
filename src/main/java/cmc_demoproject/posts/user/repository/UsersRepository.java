package cmc_demoproject.posts.user.repository;

import cmc_demoproject.posts.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query("select b from Users b where b.email = :email")
    Optional<Users> findByEmail(@Param("email")String user_id);

    boolean existsByEmail(String mail);
    Users findByUserId(Long user_id);
}
