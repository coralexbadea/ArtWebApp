package com.example.artapp.service.typecheck;


public class TypeCheckContext {
    private TypeCheckStrategy strategy;

    public void setStrategy(TypeCheckStrategy strategy) {
        this.strategy = strategy;
    }

    public Boolean check(String name){
        return strategy.check(name);
    }
}
