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
import montalvo.planclock.DAO.ContactDAO;
import montalvo.planclock.DAO.CustomerDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Appointment;
import montalvo.planclock.Model.Contact;
import montalvo.planclock.Model.FirstReport;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for Reports
 */
public class Reports implements Initializable {
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<FirstReport> allFirstReports = FXCollections.observableArrayList();
    private static User loggedUser = null;
    public TableView<Appointment> appointmentsTable;
    public TableColumn appointmentIDCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn typeCol;
    public TableColumn startDateCol;
    public TableColumn endDateCol;
    public TableColumn customerIDCol;
    public ComboBox<Contact> contactsCombo;
    public Label usernameLabel;
    public Label errorLabel;
    public RadioButton appointmentRadio;
    public RadioButton customerRadio;
    public TableView<FirstReport> monthlyTable;
    public TableColumn monthCol;
    public TableColumn monthlyTypeCol;
    public TableColumn totalCol;
    public TextField totalCustomers;
    public TextField totalAppointments;

    /**
     * Gets Logged-in user from previous screen
     * @param user
     */
    public static void getLoggedUser(User user) {
        loggedUser = user;
    }

    /**
     * loads data to generate the different reports
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointments = AppointmentDAO.getAllAppointments();

        String totalCus = Integer.toString(CustomerDAO.getAllCustomers().size());
        String totalApps = Integer.toString(appointments.size());
        totalCustomers.setText(totalCus);
        totalAppointments.setText(totalApps);

        contactsCombo.setItems(ContactDAO.getAllContacts());
        usernameLabel.setText(loggedUser.getUserName());

        generateFirstReport();

        monthCol.setCellValueFactory(new PropertyValueFactory<>("MonthString"));
        monthlyTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("Count"));
        monthlyTable.setItems(allFirstReports);
    }

    /**
     * Generates a report of appointments by month and type
     */
    public void generateFirstReport() {
        int month = 0;
        int year = 0;
        String type = "";
        Appointment app = null;

        for(int i = 0; i < appointments.size(); i++) {
            app = appointments.get(i);
            month = app.getStart().getMonth().getValue();
            year = app.getStart().getYear();
            type = app.getType();
            boolean exist = false;

            if(i == 0){
                allFirstReports.add(new FirstReport(month, year, type));
                continue;
            }

            for(FirstReport firstReport : allFirstReports) {
                if(firstReport.getMonth() == month && firstReport.getType().equals(type)) {
                    firstReport.incrementCount();
                    exist = true;
                    break;
                }
            }

            if(!exist) {
                allFirstReports.add(new FirstReport(month, year, type));
            }

        }
    }

    /**
     * Exits the application
     * @param actionEvent
     */
    public void exitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Sends you to the Customers Screen
     * @param actionEvent
     */
    public void customerRadioSelected(ActionEvent actionEvent) throws IOException {
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
     * Sends you to the Appointments Screen
     * @param actionEvent
     */
    public void appointmentRadioSelected(ActionEvent actionEvent) throws IOException {
        if (appointmentRadio.isSelected()) {
            Appointments.getLoggedUser(loggedUser);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Appointments.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Loads table with appointment data based on selected contact
     * @param actionEvent
     */
    public void contactsComboSelection(ActionEvent actionEvent) {
        int contactID = contactsCombo.getSelectionModel().getSelectedItem().getContactID();
        appointments = AppointmentDAO.getAllAppointments();

        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        appointmentsTable.setItems(appointments.filtered(app -> app.getContactID() == contactID));
    }
}
