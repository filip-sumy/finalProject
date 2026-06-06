package org.spring.finalproject.repository;

import org.spring.finalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END "
            + "FROM User u WHERE u.email = :email AND u.id <> :id")
    boolean existsByEmailAndIdNot(
            @Param("email") String email,
            @Param("id") Long id);
}
