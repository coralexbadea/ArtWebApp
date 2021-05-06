package com.example.artapp.domain;



import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="posts")
public class Post extends Object{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String ptitle;

    private String pauthor;

    private String pdescription;

    private String imagePath;

    private String modelPath;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "post")
    private Set<Review> reviews = new HashSet<>();


    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPauthor() {
        return pauthor;
    }

    public void setPauthor(String pauthor) {
        this.pauthor = pauthor;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }



    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }


}
