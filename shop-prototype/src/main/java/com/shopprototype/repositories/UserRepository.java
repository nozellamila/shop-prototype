package com.shopprototype.repositories;

import com.shopprototype.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    @Query("SELECT user FROM User user WHERE" +
            "(:id IS NULL OR user.id = :id) AND" +
            "(:name IS NULL OR user.name = :name) AND" +
            "(:email IS NULL OR user.email = :email)")
    Page<User> findByParameters(@Param("id") Integer id,
                                @Param("name") String name,
                                @Param("email") String email,
                                Pageable pageable);

    Optional<User> findByEmail(String email);
}
