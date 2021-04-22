package com.example.artapp.service;

import com.example.artapp.domain.Post;
import com.example.artapp.domain.Review;
import com.example.artapp.domain.User;
import com.example.artapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    UserService userService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    PostService postService;
    public List<Review> listAllByPostId(Long id) {
        return reviewRepository.findAllByPost_Pid(id);
    }

    public void save(Review review, Long id) {
        Post post = postService.findById(id);
        User user = userService.getCurrentUser();
        review.setUser(user);
        review.setPost(post);
        reviewRepository.save(review);

    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).get();
    }

    @Transactional
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public Boolean getPermitted(Long id) {
        Post post = postService.getPostById(id);
        User user = userService.getUserByPost(post);
        User currentUser = userService.getCurrentUser();
        if(user != currentUser){
            return false;
        }
        return true;
    }

}
