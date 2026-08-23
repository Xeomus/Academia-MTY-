package after;

public class EmailSender implements MessageSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Email is sending email to " + recipient);
        System.out.println("Message is: " + message);
    }
}
