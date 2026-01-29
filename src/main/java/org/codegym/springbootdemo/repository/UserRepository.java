package org.codegym.springbootdemo.repository;

import java.util.List;
import java.util.Optional;
import org.codegym.springbootdemo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndAge(String email, int age);

  Optional<User> findByEmailAndAgeAndFirstName(String email, int age, String firstName);

  Optional<User> findByEmailAndFirstName(String email, String firstName);

  Optional<User> findByEmailLike(String email);

  Optional<User> findByAgeGreaterThan(int age);

  @Query("""
      SELECT u FROM User u
              WHERE (:minAge is NULL OR u.age >= :minAge)
              AND (:maxAge IS NULL OR u.age <= :maxAge)
              AND (:email is NULL or LOWER( u.email ) LIKE LOWER(CONCAT('%', :email, '%') ))
      """)
  List<User> searchUsers(@Param("minAge") Integer minAge,
      @Param("maxAge") Integer maxAge,
      @Param("email") String email);
}
