package com.example.artapp.service.notification;

import java.util.ArrayList;
import java.util.List;


public abstract class Subject  {
    protected List<Observer> observers = new ArrayList<Observer>();
    protected Object state;
    public abstract void setState(Object state);
    public abstract void attach(Observer observer);
    public abstract void notifyAllObservers();
}