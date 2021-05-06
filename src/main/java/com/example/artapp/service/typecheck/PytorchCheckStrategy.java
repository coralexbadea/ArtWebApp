package com.example.artapp.service.typecheck;

public class PytorchCheckStrategy implements TypeCheckStrategy {

    @Override
    public Boolean check(String name) {
        if(name.contains(".t7")){
            return true;
        }
        return false;
    }
}
