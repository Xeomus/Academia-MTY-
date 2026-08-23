package before;

public class noInyectionDependency {

    public void sendMessage(String recipient, String message) {

        EmailSenderNoDependency emailSenderNoDependency = new EmailSenderNoDependency();

        emailSenderNoDependency.send(recipient, message);
    }
}