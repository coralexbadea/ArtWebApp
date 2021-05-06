package com.example.artapp.controller;

import com.example.artapp.domain.Review;
import com.example.artapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping(value = "/index/{id}")
    public ModelAndView getreviews(@PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("reviews", reviewService.listAllByPostId(id));
        modelAndView.addObject("id", id);
        modelAndView.setViewName("review/index");
        return modelAndView;
    }


    @PostMapping(value = "/create/{id}")
    public String create(@PathVariable(name = "id") Long id, @Valid Review review) {
        reviewService.save(review, id);
        return "redirect:/review/create/"+ id;
    }

    @GetMapping(value = "/create/{id}")
    public ModelAndView newReview(@PathVariable(name="id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("review", new Review());
        modelAndView.addObject( "pid", id);
        modelAndView.setViewName("/review/create");
        return modelAndView;
    }

    @RequestMapping("/delete/{pid}/{rid}")
    public String deletereview(@PathVariable (name="pid") Long pid,@PathVariable (name="rid") Long rid) {
        reviewService.delete(rid);
        return "redirect:/review/index/"+pid;
    }

}
