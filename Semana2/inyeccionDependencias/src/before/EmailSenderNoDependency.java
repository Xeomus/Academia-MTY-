package before;

public class EmailSenderNoDependency {

    public void send(String recipent, String message) {
        System.out.println("Sending email to " + recipent);
        System.out.println("Message is: " + message);
    }
}
