package com.example.artapp.service.impl;

import com.example.artapp.domain.Post;
import com.example.artapp.domain.Review;
import com.example.artapp.domain.User;
import com.example.artapp.repository.ReviewRepository;
import com.example.artapp.service.PostService;
import com.example.artapp.service.ReviewService;
import com.example.artapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    UserService userService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    PostService postService;
    @Override
    public List<Review> listAllByPostId(Long id) {
        return reviewRepository.findAllByPost_Pid(id);
    }

    @Override
    public void save(Review review, Long id) {
        Post post = postService.findById(id);
        User user = userService.getCurrentUser();
      //  List<Review> rev = reviewRepository.findAllByPost(post);
        Review rev = reviewRepository.findByPostAndUser(post, user);
        if(rev == null){
            review.setUser(user);
            review.setPost(post);
            reviewRepository.save(review);
        }else{
            rev.setReviewStar(review.getReviewStar());
            rev.setComment(review.getComment());
            reviewRepository.save(review);
        }

    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
//
//    public Boolean getPermitted(Long id) {
//        Post post = postService.getPostById(id);
//        User user = userService.getUserByPost(post);
//        User currentUser = userService.getCurrentUser();
//        if(user != currentUser){
//            return false;
//        }
//        return true;
//    }

}
