package com.example.itmda3_group_project.AI_Helper;

public class Message {
    public static final int TYPE_USER = 0;
    public static final int TYPE_AI = 1;

    private String text;
    private final int type;

    public Message(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public int getType() { return type; }
}
