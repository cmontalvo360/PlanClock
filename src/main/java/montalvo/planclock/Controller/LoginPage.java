package montalvo.planclock.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import montalvo.planclock.DAO.UserDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**
 * This is the controller for the Login Screen
 */
public class LoginPage implements Initializable {
    public ComboBox languageCombo;
    private ObservableList<User> userList;
    public TextField usernameField;
    public TextField passwordField;
    public Label localeLabel;
    public Label errorLabel;

    /**
     * Gets user data when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> lang = FXCollections.observableArrayList("English", "French");
        userList = UserDAO.getAllUsers();

        ZoneId zone = ZoneId.systemDefault();
        localeLabel.setText(zone.toString());
        languageCombo.setItems(lang);
    }

    /**
     * Sends you to Appointment screen is username and password are correct
     * @param actionEvent sign-in button pressed
     * @throws IOException
     */
    public void signInUser(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        for(User user : userList) {
            if(user.getUserName().equals(username) && user.getPassword().equals(password)) {
                Appointments.getLoggedUser(user);

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/Appointments.fxml"));
                Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
                stage.setTitle("Dashboard");
                stage.setScene(scene);
                stage.show();
                return;
            }
        }

        errorLabel.setText("Incorrect username or password");
    }

    /**
     * Exits the application
     * @param actionEvent exit button pressed
     */
    public void exitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Changes the Language of the application
     * @param actionEvent language selected
     */
    public void changeLanguage(ActionEvent actionEvent) {

    }
}
