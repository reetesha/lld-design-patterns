package com.reetesh.features.sealed;

//2. Implementors must be final, sealer or non-sealed
//Records are Implicitly final, so they work perfectly here
public record Email(String address, String content) implements Notification{
    @Override
    public String getMessage(){
        return "Email to "+ address +":"+content;
    }
}
