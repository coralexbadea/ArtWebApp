package com.example.artapp.service.impl;


import com.example.artapp.domain.Post;
import com.example.artapp.domain.Profile;
import com.example.artapp.domain.Role;
import com.example.artapp.domain.User;
import com.example.artapp.repository.RoleRepository;
import com.example.artapp.repository.UserRepository;
import com.example.artapp.service.ProfileService;
import com.example.artapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    ProfileService profileService;
    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserEnabled(true);
        Role userRole = roleRepository.findByRoleName("ARTIST");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        Profile profile = new Profile();
        profile.setUser(user);
        profileService.save(profile);
        user.setProfile(profile);
        return userRepository.save(user);


    }


    @Override
    @Transactional
    public List<User> listAllByRole(String role) {

        return userRepository.findAllByRoles_RoleName(role);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findUserByUserName(auth.getName());

    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findUserByUserId(id);
    }

    @Override
    public User getUserByProfile(Profile profile) {
        return userRepository.findByProfile(profile);
    }

    @Override
    public User getUserByPost(Post post) {
        return userRepository.findUserByPostsContaining(post);
    }
}
