import java.io.*;

public class DataManager {

    private static final String FILE_NAME =
            "stock_trading_data.dat";

    public static void saveUser(User user) {

        try {

            ObjectOutputStream output =
                    new ObjectOutputStream(
                            new FileOutputStream(FILE_NAME)
                    );

            output.writeObject(user);

            output.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving data: " + e.getMessage()
            );
        }
    }

    public static User loadUser() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return null;
        }

        try {

            ObjectInputStream input =
                    new ObjectInputStream(
                            new FileInputStream(FILE_NAME)
                    );

            User user = (User) input.readObject();

            input.close();

            return user;

        } catch (IOException | ClassNotFoundException e) {

            System.out.println(
                    "Error loading saved data."
            );

            return null;
        }
    }
}