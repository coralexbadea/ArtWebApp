package com.example.artapp.controller;

import com.example.artapp.domain.Post;
import com.example.artapp.service.PostService;
import com.example.artapp.service.UserService;
import com.example.artapp.service.storage.StorageFileNotFoundException;
import com.example.artapp.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    StorageService storageService;
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @GetMapping(value = "/view/{pid}")
    public ModelAndView viewpost(@PathVariable(name = "pid") Long pid){
        ModelAndView modelAndView = new ModelAndView();
        Post post = postService.findById(pid);
        modelAndView.addObject(post);
        modelAndView.setViewName("post/view");
        return modelAndView;
    }

    @GetMapping(value = "/viewPersonal/{pid}")
    public ModelAndView viewpostPersonal(@PathVariable(name = "pid") Long pid){
        ModelAndView modelAndView = new ModelAndView();
        postService.handleViewpostPersonal(modelAndView, pid);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable(name = "id") Long id) {
        ModelAndView modelAndVie = new ModelAndView("post/edit");
        Post post = postService.findById(id);
        modelAndVie.addObject("post", post);
        return modelAndVie;
    }

    @PostMapping(value = "/edit/{pid}")
    public String edit(@PathVariable(name = "pid") Long pid, @ModelAttribute("post") Post post) {
        postService.saveEdit(post);
        return "redirect:/post/view/"+pid;
    }


    @GetMapping(value = "/index/{pid}")
    public ModelAndView index(@PathVariable(name = "pid") Long pid) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("posts", postService.getPosts(pid));
        modelAndView.setViewName("/post/index");
        return modelAndView;
    }

    @GetMapping(value = "/create")
    public ModelAndView createpost() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("post", new Post());
        modelAndView.setViewName("/post/create");
        return modelAndView;
    }

    @GetMapping(value = "/createPersonal")
    public ModelAndView createpostPersonal() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("post", new Post());
        modelAndView.setViewName("/post/createPersonal");
        return modelAndView;
    }


    @PostMapping("/create")
    public String createpost(@ModelAttribute Post post,
                                @ModelAttribute MultipartFile image,
                                @ModelAttribute MultipartFile model,
                                        RedirectAttributes redirectAttributes){

       String result = postService.createPostCreate( post, image, model, redirectAttributes);
       return result;
    }

    @PostMapping("/createPersonal")
    public String createpostPersonal(
                            @ModelAttribute Post post,
                             @ModelAttribute MultipartFile image,
                             RedirectAttributes redirectAttributes){

        String result = postService.createPostPersonalCreate(post, image, redirectAttributes);
        return result;
    }



    @PostMapping("/paint/{pid}")
    public String paint(@PathVariable(name = "pid") Long pid,
                             @ModelAttribute MultipartFile image,
                             RedirectAttributes redirectAttributes) {
       String result = postService.paintCreate(image, pid, redirectAttributes);
       return result;
    }

    @RequestMapping("/delete/{pid}")
    public String deletelaboratory(@PathVariable (name="pid") Long pid) {
        postService.delete(pid);
        return "redirect:/index";
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }


}
