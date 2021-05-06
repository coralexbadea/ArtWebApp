package com.example.artapp.service.notification;

import com.example.artapp.domain.Post;
import com.example.artapp.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogObserver extends Observer {

    @Autowired
    StorageService storageService;

    public LogObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update(Object state) {
        Post post = (Post) state;
        storageService.writeToLog(post.getPtitle());
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
