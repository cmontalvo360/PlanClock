package montalvo.planclock.Controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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

public class LoginPage implements Initializable {
    private ObservableList<User> userList;
    public TextField usernameField;
    public TextField passwordField;
    public Label localeLabel;
    public Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userList = UserDAO.getAllUsers();

        ZoneId zone = ZoneId.systemDefault();
        localeLabel.setText(zone.toString());
    }

    public void signInUser(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        for(User user : userList) {
            if(user.getUserName().equals(username) && user.getPassword().equals(password)) {
                Dashboard.getLoggedUser(user);

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Dashboard.fxml"));
                Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 1300, 760);
                stage.setTitle("Dashboard");
                stage.setScene(scene);
                stage.show();
                return;
            }
        }

        errorLabel.setText("Incorrect username or password");
    }

    public void exitApp(ActionEvent actionEvent) {
        Platform.exit();
    }
}
