package montalvo.planclock.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import montalvo.planclock.DAO.AppointmentDAO;
import montalvo.planclock.DAO.CustomerDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Appointment;
import montalvo.planclock.Model.Customer;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Appointments implements Initializable {
    private static User loggedUser = null;
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

    public static void getLoggedUser(User user) {
        loggedUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        appointmentsTable.setItems(AppointmentDAO.getAllAppointments());

        usernameLabel.setText(loggedUser.getUserName());
    }

    public void addBtnClick(ActionEvent actionEvent) throws IOException {
        AddAppointment.getLoggedUser(loggedUser);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/AddAppointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 632, 629);
        stage.setScene(scene);
        stage.show();
    }

    public void editAppointment(ActionEvent actionEvent) throws IOException {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if(appointment == null) {
            errorLabel.setText("Please select a Appointment to Delete");
            return;
        } else {
            EditAppointment.getAppointment(appointment, loggedUser);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/EditAppointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteAppointment(ActionEvent actionEvent) {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if(appointment == null) {
            errorLabel.setText("Please select a Appointment to Delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Appointment");
        alert.setHeaderText("Delete");
        alert.setContentText("Are you sure you want to Delete?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            AppointmentDAO.deleteAppointment(appointment.getAppointmentID());
            appointmentsTable.getItems().remove(appointment);
        }

    }

    public void exitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void customerRadioClick(ActionEvent actionEvent) throws IOException {
        if(customerRadio.isSelected()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Dashboard.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
            stage.setScene(scene);
            stage.show();
        }
    }
}