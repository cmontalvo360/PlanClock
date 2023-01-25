package montalvo.planclock.Controller;

import javafx.fxml.Initializable;
import montalvo.planclock.Model.Appointment;
import montalvo.planclock.Model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class EditAppointment implements Initializable {
    private static User loggedUser = null;
    private static Appointment appointment = null;

    public static void getAppointment(Appointment app, User user) {
        appointment = app;
        loggedUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
