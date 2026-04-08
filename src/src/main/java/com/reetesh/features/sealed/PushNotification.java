package com.reetesh.features.sealed;

public final class PushNotification implements Notification{
    private final String deviceId;
    public PushNotification(String deviceId){this.deviceId=deviceId; }
    @Override
    public String getMessage(){
        return "Push alert to device: "+deviceId;
    }
}
