package edu.avans.hartigehap.web.form;

public class Message {

    private String type;

    private String body;

    public Message() {
    }

    public Message(String type, String body) {
        this.type = type;
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return body;
    }

    public void setMessage(String message) {
        this.body = message;
    }

}
