package com.example.artapp.service;

import com.example.artapp.domain.Review;

import javax.transaction.Transactional;
import java.util.List;

public interface ReviewService {
    List<Review> listAllByPostId(Long id);

    void save(Review review, Long id);

    Review findById(Long id);

    @Transactional
    void delete(Long id);
}
