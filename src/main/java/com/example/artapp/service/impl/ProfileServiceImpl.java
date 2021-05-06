package com.example.artapp.service.impl;

import com.example.artapp.domain.Profile;
import com.example.artapp.domain.User;
import com.example.artapp.repository.ProfileRepository;
import com.example.artapp.service.ProfileService;
import com.example.artapp.service.UserService;
import com.example.artapp.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("profileService")
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserService userService;
    @Autowired
    StorageService storageService;
    @Autowired
    ConstantsImpl cons;

    @Override
    public Profile getCurrentProfile(){
        User user = userService.getCurrentUser();
        return user.getProfile();
    }

    @Override
    public Profile getById(Long pid) {
        return profileRepository.findById(pid).get();
    }

    @Override
    public void save(Profile profile) {
        profileRepository.save(profile);
    }


    @Override
    public void delete(Long id){
        profileRepository.deleteById(id);
        storageService.deleteFolder(id);
    }

    @Override
    public List<Profile> listAll() {
        return profileRepository.findAll();
    }

    @Override
    public Profile saveEdit(Profile profile) {
        Profile p = profileRepository.findById(profile.getPid()).get();
        profile.setUser(p.getUser());
        return profileRepository.save(profile);
    }

    @Override
    public Profile getProfileById(Long pid) {
        return profileRepository.findById(pid).get();
    }

    @Override
    public List<Profile> listExplore() {

        List<Profile> profiles = profileRepository.findAll();
        Profile thisProfile = userService.getCurrentUser().getProfile();
        profiles.remove(thisProfile);
        Collections.shuffle(profiles);
        List<Profile> result = new ArrayList<>(profiles.subList(0, cons.getNumbersUsersToExplore()));
        return result;
    }

    @Override
    public Object listArtists() {
        List<Profile> profiles = profileRepository.findAll();
        return profiles;
    }
}
