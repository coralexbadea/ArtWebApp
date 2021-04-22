package com.example.artapp.controller;


import com.example.artapp.domain.Post;
import com.example.artapp.domain.User;

import com.example.artapp.service.PostService;
import com.example.artapp.service.UserService;
import com.example.artapp.service.storage.StorageService;
import com.example.artapp.viewmodel.RegistrationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private RegistrationComponent registrationModel;

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Autowired
    StorageService storageService;
    @GetMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("posts", postService.getAdminPosts());
        storageService.initPersonalFolder(userService.getCurrentUser().getUserId());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/painting/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/painting/personal/{pid}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> servePersonal(@PathVariable(name="pid") Long pid, @PathVariable String filename) {

        Resource file = storageService.loadAsResourcePersonal(filename, pid);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }



    @GetMapping("/painting/personal/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveDownload(@PathVariable String filename) {

        Resource file = storageService.loadAsResourcePersonal(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }




    @GetMapping(value = {"/", "/login"})
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value = "/registration")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new User());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("registration", registrationModel.create(user, bindingResult) );
        return modelAndView;
    }






}
