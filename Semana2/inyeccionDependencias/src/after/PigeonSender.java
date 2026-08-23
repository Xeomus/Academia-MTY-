package after;

public class PigeonSender implements MessageSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Pigeon is Sending mail to " + recipient);
        System.out.println("Message is: " + message);
    }
}
