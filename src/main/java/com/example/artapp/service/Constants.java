package com.example.artapp.service;

import org.springframework.stereotype.Service;

@Service("cons")
public class Constants {
    private int numbersUsersToExplore = 1;

    public int getNumbersUsersToExplore() {
        return numbersUsersToExplore;
    }
}
