package montalvo.planclock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import montalvo.planclock.DAO.JDBC;

import java.io.IOException;

/**
 * This is the main class. This is where my Program starts.
 */
public class Main extends Application {
    /**
     * This is the start method that is called when the launch method is called.
     * This is where the first fxml is initialized.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This is the main line of program. This is the line of code that is called when the program starts.
     * @param args
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }

}