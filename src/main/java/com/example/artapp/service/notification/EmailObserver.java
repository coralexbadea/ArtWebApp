package com.example.artapp.service.notification;

import com.example.artapp.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailObserver extends Observer {

    @Autowired
    SendMail sendMail;
    public EmailObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update(Object state) {
        Post post = (Post) state;
        sendMail.sendMail(post.getPtitle());

    }

    @Override
    public void setSubject(Subject s) {
        this.subject = s;
    }
}
