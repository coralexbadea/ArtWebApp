package com.example.artapp.service.notification;

import org.springframework.stereotype.Service;

@Service("mainPostSubject")
public class MainPostSubject extends Subject {

    public void setState(Object state) {
        this.state = state;
        notifyAllObservers();
    }

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update(this.state);
        }
    }


}
