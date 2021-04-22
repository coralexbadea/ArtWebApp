package com.example.artapp.security;


import com.example.artapp.domain.User;
import com.example.artapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String user_name) throws UsernameNotFoundException {
        User user  = userRepository.findByUserName(user_name);
        if(user == null){
            throw new UsernameNotFoundException("User not found.");
        }
        return new MyUserDetails(user);
    }
}
