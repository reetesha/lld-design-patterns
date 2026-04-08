package com.reetesh.features.sealed;

public class NotificationService {
    public void send(Notification note){
        String status = switch (note){
            case Email e when e.address().isBlank() ->  "Error : cannot send email. Addressis empty!";
            case Email e ->  "Commecting to SMTP Server for "+e.address();
            case SMS s -> "Contacting to SMS Gateway .."+s.phoneNumber();
            case PushNotification p-> "Pinging Firecase/APNS ..";
            case null, default -> "Invalid of missing Notification";
        };
        System.out.println(status);
        System.out.println(note.getMessage());
    }
    public static void main(String args[]){
        NotificationService service = new NotificationService();

        Notification myEmail= new Email("test@example.com", "Hello Java !");
        service.send(myEmail);

        Notification mySms= new SMS("9876543210", "code: 1234");
        service.send(mySms);

        service.send(new Email("", "This should fail!"));
        service.send(new Email("test@example.com", "This should pass!"));
    }
}
