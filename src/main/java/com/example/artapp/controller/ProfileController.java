package com.example.artapp.controller;

import com.example.artapp.domain.Profile;
import com.example.artapp.service.PostService;
import com.example.artapp.service.ProfileService;
import com.example.artapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;


    @GetMapping("/view")
    public ModelAndView view() {
        ModelAndView modelAndVie = new ModelAndView("profile/view");
        modelAndVie.addObject("profile", profileService.getCurrentProfile());
        return modelAndVie;
    }

    @GetMapping("/view/{pid}")
    public ModelAndView view(@PathVariable(name = "pid") Long pid) {
        ModelAndView modelAndVie = new ModelAndView("profile/viewOthers");
        modelAndVie.addObject("profile", profileService.getById(pid));
        return modelAndVie;
    }



    @GetMapping(value = "/index")
    public ModelAndView getprofiles() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("profiles", profileService.listAll());
        modelAndView.setViewName("profile/index");
        return modelAndView;
    }

    @GetMapping(value = "/explore")
    public ModelAndView explore() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("profiles", profileService.listExplore());
        modelAndView.setViewName("profile/explore");
        return modelAndView;
    }


    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable(name = "id") Long id) {
        ModelAndView modelAndVie = new ModelAndView("profile/edit");
        modelAndVie.addObject("profile", profileService.getById(id));
        return modelAndVie;
    }
    
    @PostMapping(value = "/edit/{id}")
    public String edit(@PathVariable(name = "id") Long id, @ModelAttribute("profile") Profile profile) {
        profileService.saveEdit(profile);
        return "redirect:/profile/edit/"+id;
    }

    @RequestMapping("/delete/{pid}")
    public String deleteProfile(@PathVariable (name="pid") Long pid) {
        profileService.delete(pid);
        return "redirect:/profile/index";
    }
}
