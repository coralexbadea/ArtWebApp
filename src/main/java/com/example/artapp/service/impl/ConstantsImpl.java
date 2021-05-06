package com.example.artapp.service.impl;

import com.example.artapp.service.Constants;
import org.springframework.stereotype.Service;

@Service("cons")
public class ConstantsImpl implements Constants {
    private int numbersUsersToExplore = 1;

    public int getNumbersUsersToExplore() {
        return numbersUsersToExplore;
    }
}
