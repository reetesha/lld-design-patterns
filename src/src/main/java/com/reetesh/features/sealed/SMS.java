package com.reetesh.features.sealed;

public record SMS(String phoneNumber, String body) implements Notification{
    @Override
    public String getMessage(){
        return "SMS to "+phoneNumber+":"+body;
    }
}
