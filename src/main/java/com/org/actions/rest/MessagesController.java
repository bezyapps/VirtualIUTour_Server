package com.org.actions.rest;

import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import java.util.Collection;

/**
 * Created by ericbhatti on 11/18/15.
 */
public class MessagesController implements ModelDriven<Object> {

    private static final long serialVersionUID = 89268916175477696L;
    private Message model = new Message();
    private String id;
    private Collection<Message> list;

    public HttpHeaders create() {
        MessageService.save(model);

        return new DefaultHttpHeaders("create");
    }

    public HttpHeaders destroy() {
        return new DefaultHttpHeaders("destroy");
    }

    public HttpHeaders show() {
        return new DefaultHttpHeaders("show").disableCaching();
    }

    public HttpHeaders update() {
        MessageService.save(model);
        return new DefaultHttpHeaders("update");
    }

    public HttpHeaders index() {
        list = MessageService.findAll();
        return new DefaultHttpHeaders("index").disableCaching();
    }

    public Object getModel() {
        return (list != null ? list : model);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null) {
            this.model = MessageService.find(id);
        }
        this.id = id;
    }
}