package com.example.artapp.domain;

import javax.persistence.*;

@Entity
@Table(name="reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    private ReviewStar reviewStar;

    private String comment;


    @ManyToOne
    @JoinColumn(name="pid")
    private Post post;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public ReviewStar getReviewStar() {
        return reviewStar;
    }

    public void setReviewStar(ReviewStar reviewStar) {
        this.reviewStar = reviewStar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
