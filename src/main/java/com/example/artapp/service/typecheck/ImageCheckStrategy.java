package com.example.artapp.service.typecheck;


public class ImageCheckStrategy implements TypeCheckStrategy{

    @Override
    public Boolean check(String name) {
        if(name.contains(".jpg") || name.contains(".jpeg") || name.contains(".png")){
            return true;
        }
        return false;
    }
}
