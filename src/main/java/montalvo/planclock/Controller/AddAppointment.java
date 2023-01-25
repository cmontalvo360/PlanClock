package montalvo.planclock.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import montalvo.planclock.DAO.AppointmentDAO;
import montalvo.planclock.DAO.ContactDAO;
import montalvo.planclock.DAO.CustomerDAO;
import montalvo.planclock.DAO.UserDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Appointment;
import montalvo.planclock.Model.Customer;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddAppointment implements Initializable {
    private static User loggedUser = null;
    public Label errorLabel;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField typeField;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public ComboBox contactCombo;
    public ComboBox customerCombo;
    public ComboBox userCombo;

    public static void getLoggedUser(User user) {
        loggedUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCombo.setItems(ContactDAO.getAllContacts());
        customerCombo.setItems(CustomerDAO.getAllCustomers());
        userCombo.setItems(UserDAO.getAllUsers());
    }

    public void addBtnClicked(ActionEvent actionEvent) throws IOException {
        int id = AppointmentDAO.getAllAppointments().size() + 1;
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime createDate = LocalDateTime.now();
        String createdBy = loggedUser.getUserName();
        Timestamp lastUpdated = Timestamp.valueOf(createDate);
        String lastUpdatedBy = loggedUser.getUserName();
        int customerID = customerCombo.getSelectionModel().getSelectedIndex() +1;
        int userID = customerCombo.getSelectionModel().getSelectedIndex() + 1;
        int contactID = contactCombo.getSelectionModel().getSelectedIndex() + 1;

        String error = "Exception: ";
        boolean exceptions = false;

        if(title.isBlank()) {
            exceptions = true;
            error += "Name is empty!\n";
        }
        if(description.isBlank()) {
            exceptions = true;
            error += "Address is empty!\n";
        }
        if(location.isBlank()) {
            exceptions = true;
            error += "Postal Code is empty!\n";
        }
        if(type.isBlank()) {
            exceptions = true;
            error += "Phone is empty!\n";
        }
        if(customerCombo.getSelectionModel().getSelectedItem() == null ) {
            exceptions = true;
            error += "No Customer was chosen!\n";
        }
        if(userCombo.getSelectionModel().getSelectedItem() == null ) {
            exceptions = true;
            error += "No user was chosen!\n";
        }
        if(contactCombo.getSelectionModel().getSelectedItem() == null ) {
            exceptions = true;
            error += "No contact was chosen!\n";
        }

        if(exceptions) {
            errorLabel.setText(error);
            return;
        }

        Appointment app = new Appointment(id, title, description, location, type, start, end, createDate, createdBy, lastUpdated,
                lastUpdatedBy, customerID, userID, contactID);
        AppointmentDAO.addAppointment(app);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Appointments.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
        stage.setScene(scene);
        stage.show();
    }

    public void cancelBtnClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Appointments.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
        stage.setScene(scene);
        stage.show();
    }
}
