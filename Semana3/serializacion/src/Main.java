import java.io.*;

public class Main {

    public static void main(String[] args) {

        Player player = new Player(
                "Alejandro Zendejas",
                10,
                "Extremo Derecho"
        );

        // SERIALIZAR
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream("player.ser"))) {

            out.writeObject(player);

            System.out.println("Player serializated.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // DESERIALIZAR
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream("player.ser"))) {

            Player playerSaved =
                    (Player) in.readObject();

            System.out.println("Player Saved:");
            System.out.println(playerSaved);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}