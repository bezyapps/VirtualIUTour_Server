package com.org.actions.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ericbhatti on 11/18/15.
 */
public class MessageService {

    public static Map messages = new HashMap();
    private static int nextMessageId = 4;

    static {
        Message message1 = new Message("1", "hello", "john");
        Message message2 = new Message("2", "world", "ted");
        Message message3 = new Message("3", "rest", "sam");
        messages.put("1", message1);
        messages.put("2", message2);
        messages.put("3", message3);
    }

    public static List findAll() {
        return new ArrayList(messages.values());
    }

    public static Message find(String id) {
        return (Message) messages.get(id);
    }

    public static void save(Message message) {
        if (message.getId() == null) {
            String id = String.valueOf(nextMessageId);
            message.setId(id);
            nextMessageId++;
        }
        messages.put(message.getId(), message);
    }

    public static void remove(String id) {
        messages.remove(id);
    }
}
