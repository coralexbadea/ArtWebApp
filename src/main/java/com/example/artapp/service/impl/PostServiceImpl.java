package com.example.artapp.service.impl;

import com.example.artapp.domain.*;
import com.example.artapp.service.notification.MainPostSubject;
import com.example.artapp.repository.PostRepository;
import com.example.artapp.repository.RoleRepository;
import com.example.artapp.repository.UserRepository;
import com.example.artapp.service.PostService;
import com.example.artapp.service.ProfileService;
import com.example.artapp.service.ReviewService;
import com.example.artapp.service.UserService;
import com.example.artapp.service.notification.Subject;
import com.example.artapp.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service("postService")
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    StorageService storageService;
    @Autowired
    ProfileService profileService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ReviewService reviewService;
    @Autowired
    Subject mainPostSubject;

    public List<Post> listAll() {
        return postRepository.findAll();
    }

    public List<Post> getAdminPosts() {

        List<Post> posts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            if(post.getUser().getRoles().contains(roleRepository.findByRoleName("ADMIN"))){

                result.add(post);
            }
        }
        storageService.initPersonalFolder(userService.getCurrentUser().getUserId());
        return result;
    }


    public void save(Post p) {
        User user = userService.getCurrentUser();
        p.setUser(user);
        postRepository.save(p);


    }

    public Post findById(Long id) {
        return postRepository.findById(id).get();
    }



    public Post saveEdit(Post post) {
        Post p = postRepository.findById(post.getPid()).get();
        p.setPauthor(post.getPauthor());
        p.setPdescription(post.getPdescription());
        p.setPtitle(post.getPtitle());
        return postRepository.save(p);
    }

    public void delete(Long id){
        Post post = postRepository.findById(id).get();
        User user = post.getUser();
        String imagePath = post.getImagePath();
        String modelPath = post.getModelPath();
        if(modelPath == null){
            storageService.deleteFile(imagePath, user.getUserId() );
        }else{
            storageService.deleteFile(imagePath, modelPath);
        }

        postRepository.deleteById(id);
    }


    public void paintImage(MultipartFile image, Long pid) throws IOException {
        Post post = postRepository.findById(pid).get();
        String model = post.getModelPath();
        storageService.store(image, model);
    }

    public String createPostCreate(Post post, MultipartFile image, MultipartFile model,
                                   RedirectAttributes redirectAttributes) {

        try{

            String modelPath = storageService.store(model);
            post.setModelPath(modelPath);
            String imagePath = storageService.store(image);
            post.setImagePath(imagePath);

            this.save(post);
            mainPostSubject.setState(post);


        }catch(Exception e){
            redirectAttributes.addFlashAttribute("message",
                    "An error has occured!\n" + e.getMessage());
            return "redirect:/post/create";
        }


        redirectAttributes.addFlashAttribute("message",
                "You successfully created post " + post.getPtitle() + "!");

        return "redirect:/index";
    }

    public String paintCreate(MultipartFile image, Long pid, RedirectAttributes redirectAttributes) {
        try {
            this.paintImage(image, pid);
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("message",
                    "An error has occured\n!" + e.getMessage());
            return ("redirect:/post/view/"+pid);

        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully painted the  image !");

        return ("redirect:/post/view/"+pid);
    }

    public List<Post> getPosts(Long pid) {
        Profile profile = profileService.getProfileById(pid);
        User user = userService.getUserByProfile(profile);
        List<Post> posts = postRepository.findAllByUser(user);

        return posts;
    }

    public String createPostPersonalCreate(Post post, MultipartFile image, RedirectAttributes redirectAttributes) {
        try{
            User user = userService.getCurrentUser();
            String imagePath = storageService.storePersonal(image, user.getUserId());
            post.setImagePath(imagePath);

            this.save(post);


        }catch(Exception e){
            redirectAttributes.addFlashAttribute("message",
                    "An error has occured!\n" + e.getMessage());
            return "redirect:/post/createPersonal";
        }


        redirectAttributes.addFlashAttribute("message",
                "You successfully created post " + post.getPtitle() + "!");

        return "redirect:/profile/view";
    }

    public Post getPostById(Long pid) {
        return postRepository.findById(pid).get();
    }

    public float computeAverageStars(Long pid){
        int sum = 0;
        List<Review> reviews = reviewService.listAllByPostId(pid);
        for (Review review: reviews) {
            sum += (ReviewStar.valueOf(review.getReviewStar().name()).ordinal()+1);
        }
        if(reviews.size() == 0){
            return 0;
        }
        return sum/reviews.size();
    }

    public void handleViewpostPersonal(ModelAndView modelAndView, Long pid) {
        Post post = this.findById(pid);
        modelAndView.addObject(post);
        User user = userService.getUserByPost(post);
        User currentUser = userService.getCurrentUser();
        float averageStars = this.computeAverageStars(pid);
        modelAndView.addObject("averageStars", averageStars);
        if(user == currentUser){
            modelAndView.setViewName("post/viewPersonal");
        }
        else {
            modelAndView.setViewName("post/viewOthers");
        }
    }
}
