package montalvo.planclock.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {

    public TextField usernameField;
    public TextField passwordField;
    public Label localeLabel;
    public Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void signInUser(ActionEvent actionEvent) {
    }

    public void exitApp(ActionEvent actionEvent) {
    }
}
