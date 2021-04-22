package com.example.artapp.repository;


import com.example.artapp.domain.Post;
import com.example.artapp.domain.Profile;
import com.example.artapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   User findByUserName(String userName);

    List<User> findAllByRoles_RoleName(String role);

    User findUserByUserId(Long pid);

    User findByProfile(Profile profile);

    User findUserByPostsContaining(Post post);
}

