package com.reetesh.features.sealed;

//1. Define the sealed interface and permitted implementers
public sealed interface Notification permits Email, SMS, PushNotification{
    String getMessage();
}


