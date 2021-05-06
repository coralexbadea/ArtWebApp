package com.example.artapp.service.notification;

public abstract class Observer {
    protected Subject subject;
    public abstract void update(Object state);
    public abstract void setSubject(Subject s);
}