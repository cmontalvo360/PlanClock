package montalvo.planclock.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import montalvo.planclock.DAO.AppointmentDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Appointment;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is a controller for the Appointments Screen
 */
public class Appointments implements Initializable {
    private static User loggedUser = null;
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public Label errorLabel;
    public TableView<Appointment> appointmentsTable;
    public TableColumn appointmentIDCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startDateCol;
    public TableColumn endDateCol;
    public TableColumn customerIDCol;
    public TableColumn userIDCol;
    public Label usernameLabel;
    public RadioButton customerRadio;
    public ToggleGroup viewToggleGrp;
    public RadioButton viewAllRadio;
    public RadioButton monthRadio;
    public RadioButton weekRadio;
    public RadioButton reportRadio;

    /**
     * Gets Logged-in user from previous screen
     * @param user
     */
    public static void getLoggedUser(User user) {
        loggedUser = user;
    }

    /**
     * Loads the tables with data from the database when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointments = AppointmentDAO.getAllAppointments();
        LocalDateTime now = LocalDateTime.now();
        boolean appToday = false;

        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        appointmentsTable.setItems(appointments);

        usernameLabel.setText(loggedUser.getUserName());

        for(Appointment app: appointments) {
            if(app.getStart().isAfter(now) && app.getStart().isBefore(now.plusMinutes(15))) {
                errorLabel.setText("You have an appointment in the next 15 min!\nAppointment ID: " + app.getAppointmentID() + "\nDate/Time: " + app.getStart());
                appToday = true;
            }
        }

        if(!appToday) {
            errorLabel.setText("You have no appointments in the next 15 min");
        }
    }

    /**
     * Sends you to Add Appointment Screen
     * @param actionEvent add button pressed
     * @throws IOException
     */
    public void addBtnClick(ActionEvent actionEvent) throws IOException {
        AddAppointment.getLoggedUser(loggedUser);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/AddAppointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 632, 629);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends you to Edit Appointment Screen
     * @param actionEvent edit button pressed
     * @throws IOException
     */
    public void editAppointment(ActionEvent actionEvent) throws IOException {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if(appointment == null) {
            errorLabel.setText("Please select a Appointment to Edit");
            return;
        } else {
            EditAppointment.getAppointment(appointment, loggedUser);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/EditAppointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 632, 629);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes an appointment if requirements are met
     * @param actionEvent cancel button pressed
     */
    public void deleteAppointment(ActionEvent actionEvent) {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if(appointment == null) {
            errorLabel.setText("Please select a Appointment to Delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Appointment");
        alert.setHeaderText("Appointment ID:  " + appointment.getAppointmentID() + "\n" +
                            "Appointment Type:  "  + appointment.getType());
        alert.setContentText("Are you sure you want to Cancel this appointment?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            AppointmentDAO.deleteAppointment(appointment.getAppointmentID());
            appointmentsTable.getItems().remove(appointment);
        }

    }

    /**
     * Exits the application
     * @param actionEvent exit button pressed
     */
    public void exitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Sends you to the Customers Screen
     * @param actionEvent customer button pressed
     * @throws IOException
     */
    public void customerRadioClick(ActionEvent actionEvent) throws IOException {
        if(customerRadio.isSelected()) {
            Customers.getLoggedUser(loggedUser);
            
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/Customers.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Sends you to the Reports Screen
     * @param actionEvent reports radio selected
     * @throws IOException
     */
    public void reportRadioSelected(ActionEvent actionEvent) throws IOException {
        if(reportRadio.isSelected()) {
            Reports.getLoggedUser(loggedUser);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/Reports.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Show all the appointments
     * @param actionEvent viewAll radio selected
     */
    public void viewAllClicked(ActionEvent actionEvent) {
        appointmentsTable.setItems(appointments);
    }


    /**
     * Show appointments for this month
     * Used a lambda expression to filter list by current month
     * @param actionEvent month radio selected
     */
    public void monthRadioClicked(ActionEvent actionEvent) {
        Month currentMonth = LocalDateTime.now().getMonth();
        if(monthRadio.isSelected()) {
            appointmentsTable.setItems(appointments.filtered(app -> app.getStart().getMonth() == currentMonth));
        }
    }

    /**
     * Show appointments for this week
     * @param actionEvent
     */
    public void weekRadioClicked(ActionEvent actionEvent) {
        ObservableList<Appointment> currWeekAppointments = FXCollections.observableArrayList();
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        LocalDate monday = LocalDate.now().minusDays(dayOfWeek - 1);
        LocalDate sunday = monday.plusDays(6);

        for (Appointment appointment : appointments){
            LocalDate appStartDate = appointment.getStart().toLocalDate();
            LocalDate appEndDate = appointment.getEnd().toLocalDate();
            if (appStartDate.compareTo(monday) * appStartDate.compareTo(sunday) <= 0){
                currWeekAppointments.add(appointment);
            } else if (appEndDate.compareTo(monday) * appEndDate.compareTo(sunday) <= 0){
                currWeekAppointments.add(appointment);
            }
        }

        appointmentsTable.setItems(currWeekAppointments);
    }
}
