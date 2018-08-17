package com.example.amas.messenger;

public class ChatMessageItem {
    String text;
    String toID;
    String fromID;
    Long timeStamp;

    public ChatMessageItem(){

    }

    public ChatMessageItem(String text, String toID, String fromID, Long timeStamp){
        this.text = text;
        this.toID = toID;
        this.fromID = fromID;
        this.timeStamp = timeStamp;
    }

}
