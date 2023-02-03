package montalvo.planclock.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import montalvo.planclock.Util.AppointmentUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * This class is the controller for the Edit Appointments Screen
 */
public class EditAppointment implements Initializable {
    private static User loggedUser = null;
    private static Appointment appointment = null;
    ObservableList<Customer> customers = FXCollections.observableArrayList();
    ObservableList<Contact> contacts = FXCollections.observableArrayList();
    ObservableList<User> users = FXCollections.observableArrayList();
    public TextField appointmentIDField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField typeField;
    public ComboBox<Contact> contactCombo;
    public ComboBox<Customer> customerCombo;
    public ComboBox<User> userCombo;
    public Label errorLabel;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public Spinner startHourSpinner;
    public Spinner endHourSpinner;
    public Spinner startMinuteSpinner;
    public Spinner endMinuteSpinner;

    /**
     * Gets the logged-in user and selected appointment from previous screen
     * @param app
     * @param user
     */
    public static void getAppointment(Appointment app, User user) {
        appointment = app;
        loggedUser = user;
    }

    /**
     * loads fields with data from selected appointment
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contacts = ContactDAO.getAllContacts();
        customers = CustomerDAO.getAllCustomers();
        users = UserDAO.getAllUsers();
        int customerID = 0;
        int contactID = 0;
        int userID = 0;

        for(Customer cust : customers) {
            if(cust.getCustomerID() == appointment.getCustomerID()) {
                customerID = customers.indexOf(cust);
            }
        }

        for(Contact contact : contacts) {
            if(contact.getContactID() == appointment.getContactID()) {
                contactID = contacts.indexOf(contact);
            }
        }

        for(User user : users) {
            if(user.getUserID() == appointment.getUserID()) {
                userID = users.indexOf(user);
            }
        }

        contactCombo.setItems(contacts);
        customerCombo.setItems(customers);
        userCombo.setItems(users);

        SpinnerValueFactory<Integer> startHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,23);
        SpinnerValueFactory<Integer> startMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,59);
        SpinnerValueFactory<Integer> endHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,23);
        SpinnerValueFactory<Integer> endMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(00,59);

        startHourValueFactory.setValue(appointment.getStart().getHour());
        startMinuteValueFactory.setValue(appointment.getStart().getMinute());
        endHourValueFactory.setValue(appointment.getEnd().getHour());
        endMinuteValueFactory.setValue(appointment.getEnd().getMinute());

        startHourSpinner.setValueFactory(startHourValueFactory);
        startMinuteSpinner.setValueFactory(startMinuteValueFactory);
        endHourSpinner.setValueFactory(endHourValueFactory);
        endMinuteSpinner.setValueFactory(endMinuteValueFactory);

        appointmentIDField.setText(Integer.toString(appointment.getAppointmentID()));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        typeField.setText(appointment.getType());
        startDatePicker.setValue(appointment.getStart().toLocalDate());
        endDatePicker.setValue(appointment.getEnd().toLocalDate());
        contactCombo.getSelectionModel().select(contactID);
        customerCombo.getSelectionModel().select(customerID);
        userCombo.getSelectionModel().select(userID);
    }

    /**
     * Saves Appointment and sends you to Appointment Screen if requirements are met
     * @param actionEvent save button pressed
     * @throws IOException
     */
    public void saveBtnClicked(ActionEvent actionEvent) throws IOException {
        int id = appointment.getAppointmentID();
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
        LocalDateTime createDate = appointment.getCreateDate();
        String createdBy = appointment.getCreatedBy();
        Timestamp lastUpdated = Timestamp.valueOf(LocalDateTime.now());
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

        String appError = AppointmentUtil.appChecker(app);
        if(appError == "") {
            AppointmentDAO.updateAppointment(id, app);
        } else {
            errorLabel.setText("Error: " + appError);
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Appointments.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends you back to the Appointments Screen without saving
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
