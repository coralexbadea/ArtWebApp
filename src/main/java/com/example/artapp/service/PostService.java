package com.example.artapp.service;

import com.example.artapp.domain.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

public interface PostService {
    public List<Post> getAdminPosts();
    public void save(Post p) ;
    public Post findById(Long id);
    public Post saveEdit(Post post) ;
    public void delete(Long id);
    public void paintImage(MultipartFile image, Long pid) throws IOException ;
    public String createPostCreate(Post post, MultipartFile image, MultipartFile model,
                                   RedirectAttributes redirectAttributes) ;

    public String paintCreate(MultipartFile image, Long pid, RedirectAttributes redirectAttributes);
    public List<Post> getPosts(Long pid) ;
    public String createPostPersonalCreate(Post post, MultipartFile image, RedirectAttributes redirectAttributes);
    public Post getPostById(Long pid);
    public float computeAverageStars(Long pid);
    public void handleViewpostPersonal(ModelAndView modelAndView, Long pid) ;
}
