package com.example.amas.messenger;

public class ChatMessageItem {
    String text;
    String type;
    Long timeStamp;

    public ChatMessageItem(){

    }

    public ChatMessageItem(String text, String type, Long timeStamp){
        this.text = text;
        this.type = type;
        this.timeStamp = timeStamp;
    }

}
