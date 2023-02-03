package montalvo.planclock.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import montalvo.planclock.DAO.AppointmentDAO;
import montalvo.planclock.DAO.ContactDAO;
import montalvo.planclock.DAO.CustomerDAO;
import montalvo.planclock.DAO.UserDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Appointment;
import montalvo.planclock.Model.Contact;
import montalvo.planclock.Model.Customer;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * This class is the controller for the Add Appointment Screen.
 */
public class AddAppointment implements Initializable {
    private static User loggedUser = null;
    public Label errorLabel;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField typeField;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public ComboBox<Contact> contactCombo;
    public ComboBox<Customer> customerCombo;
    public ComboBox<User> userCombo;
    public Spinner startHourSpinner;
    public Spinner endHourSpinner;
    public Spinner startMinuteSpinner;
    public Spinner endMinuteSpinner;

    /**
     * Gets the logged-in user info from other screen
     * @param user
     */
    public static void getLoggedUser(User user) {
        loggedUser = user;
    }

    /**
     * loads the input fields with data when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> startHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,23);
        SpinnerValueFactory<Integer> startMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,59);
        SpinnerValueFactory<Integer> endHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,23);
        SpinnerValueFactory<Integer> endMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,59);

        startHourSpinner.setValueFactory(startHourValueFactory);
        startMinuteSpinner.setValueFactory(startMinuteValueFactory);
        endHourSpinner.setValueFactory(endHourValueFactory);
        endMinuteSpinner.setValueFactory(endMinuteValueFactory);

        contactCombo.setItems(ContactDAO.getAllContacts());
        customerCombo.setItems(CustomerDAO.getAllCustomers());
        userCombo.setItems(UserDAO.getAllUsers());
    }

    /**
     * Creates a new Appointment and sends you back to Appointment Screen is requirements are met
     * @param actionEvent add button pressed
     * @throws IOException
     */
    public void addBtnClicked(ActionEvent actionEvent) throws IOException {
        int id = AppointmentDAO.getAllAppointments().size() + 1;
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        LocalDate ldStart = startDatePicker.getValue();
        LocalTime ltStart = LocalTime.of((int)startHourSpinner.getValue(), (int)startMinuteSpinner.getValue());
        LocalDateTime start = LocalDateTime.of(ldStart, ltStart);
        LocalDate ldEnd = endDatePicker.getValue();
        LocalTime ltEnd = LocalTime.of((int)endHourSpinner.getValue(), (int)endMinuteSpinner.getValue());
        LocalDateTime end = LocalDateTime.of(ldEnd, ltEnd);
        LocalDateTime createDate = LocalDateTime.now();
        String createdBy = loggedUser.getUserName();
        Timestamp lastUpdated = Timestamp.valueOf(createDate);
        String lastUpdatedBy = loggedUser.getUserName();
        int customerID = customerCombo.getSelectionModel().getSelectedItem().getCustomerID();
        int userID = userCombo.getSelectionModel().getSelectedItem().getUserID();
        int contactID = contactCombo.getSelectionModel().getSelectedItem().getContactID();

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

    /**
     * Sends you back to Appointment Screen without saving
     * @param actionEvent cancel button pressed
     * @throws IOException
     */
    public void cancelBtnClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Appointments.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
        stage.setScene(scene);
        stage.show();
    }
}
