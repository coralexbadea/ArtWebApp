package com.example.artapp.service;

import com.example.artapp.domain.Profile;

import java.util.List;

public interface ProfileService {
    Profile getCurrentProfile();

    Profile getById(Long pid);

    void save(Profile profile);

    void delete(Long id);

    List<Profile> listAll();

    Profile saveEdit(Profile profile);

    Profile getProfileById(Long pid);

    List<Profile> listExplore();

    Object listArtists();
}
