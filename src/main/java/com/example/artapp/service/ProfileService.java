package com.example.artapp.service;

import com.example.artapp.domain.Profile;
import com.example.artapp.domain.User;
import com.example.artapp.repository.ProfileRepository;
import com.example.artapp.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserService userService;
    @Autowired
    StorageService storageService;
    @Autowired
    Constants cons;

    public Profile getCurrentProfile(){
        User user = userService.getCurrentUser();
        return user.getProfile();
    }

    public Profile getById(Long pid) {
        return profileRepository.findById(pid).get();
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }


    public void delete(Long id){

        profileRepository.deleteById(id);
        storageService.deleteFolder(id);
    }

    public List<Profile> listAll() {
        return profileRepository.findAll();
    }

    public Profile saveEdit(Profile profile) {
        Profile p = profileRepository.findById(profile.getPid()).get();
        profile.setUser(p.getUser());
        return profileRepository.save(profile);
    }

    public Profile getProfileById(Long pid) {
        return profileRepository.findById(pid).get();
    }

    public List<Profile> listExplore() {

        List<Profile> profiles = profileRepository.findAll();
        Profile thisProfile = userService.getCurrentUser().getProfile();
        profiles.remove(thisProfile);
        Collections.shuffle(profiles);
        List<Profile> result = new ArrayList<>(profiles.subList(0, cons.getNumbersUsersToExplore()));
        return result;
    }
}
