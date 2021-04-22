package com.example.artapp.viewmodel;


import com.example.artapp.domain.User;
import com.example.artapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.HashMap;

@Component
public class RegistrationComponent {

    @Autowired
    UserService userService;

    public HashMap<String, Object> create(User user, BindingResult bindingResult){
        HashMap<String, Object> model = new HashMap<>();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (!bindingResult.hasErrors()) {
            userService.saveUser(user);
            model.put("successMessage", "User has been registered successfully");
            model.put("user", new User());
        }
        return model;
    }

}
