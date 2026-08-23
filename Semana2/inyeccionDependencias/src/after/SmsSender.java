package after;

public class SmsSender implements MessageSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Sms is sending message to " + recipient);
        System.out.println("Message is: " + message);
    }
}
