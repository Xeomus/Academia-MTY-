package after;

public class MessageService {

    private final MessageSender messageSender;

    public MessageService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void send(String recipient, String message) {
        messageSender.send(recipient, message);
    }
}
