package com.example.artapp.service;

import com.example.artapp.domain.Post;
import com.example.artapp.domain.Profile;
import com.example.artapp.domain.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService {
    User findUserByUserName(String userName);

    User saveUser(User user);

    @Transactional
    List<User> listAllByRole(String role);

    User findById(Long id);

    void delete(Long id);

    User getCurrentUser();

    User getUserById(Long id);

    User getUserByProfile(Profile profile);

    User getUserByPost(Post post);
}
