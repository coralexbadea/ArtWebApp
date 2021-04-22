package com.example.artapp.repository;

import com.example.artapp.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByPost_Pid(Long id);

    @Modifying
    @Query("delete from Review r where r.rid=:id")
    void deleteById(@Param("id") Long id);
}
