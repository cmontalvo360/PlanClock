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
import montalvo.planclock.DAO.CustomerDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Appointment;
import montalvo.planclock.Model.Customer;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is a controller for the Customers Screen
 */
public class Customers implements Initializable {
    private static User loggedUser = null;
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public TableView<Customer> customerTable;
    public TableColumn<Customer, Integer> customerID;
    public TableColumn customerName;
    public TableColumn customerPhone;
    public TableColumn customerAddress;
    public TableColumn customerPostal;
    public TableColumn customerCreateDate;
    public TableColumn customerCreatedBy;
    public TableColumn customerLastUpdate;
    public TableColumn customerLastUpdateBy;
    public TableColumn customerDivision;
    public Label errorLabel;
    public Label usernameLabel;
    public RadioButton appointmentRadio;
    public RadioButton customerRadio;
    public ToggleGroup viewToggleGrp;

    /**
     * Get logged-in user from previous screen
     * @param user
     */
    public static void getLoggedUser(User user) {
        loggedUser = user;
    }

    /**
     * loads tables with data from database when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointments = AppointmentDAO.getAllAppointments();

        customerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPostal.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        customerCreateDate.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));
        customerCreatedBy.setCellValueFactory(new PropertyValueFactory<>("CreatedBy"));
        customerLastUpdate.setCellValueFactory(new PropertyValueFactory<>("LastUpdated"));
        customerLastUpdateBy.setCellValueFactory(new PropertyValueFactory<>("LastUpdateBy"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("DivisionID"));
        customerTable.setItems(CustomerDAO.getAllCustomers());

        usernameLabel.setText(loggedUser.getUserName());
    }

    /**
     * Sends you to Add Customer Screen
     * @param actionEvent add button pressed
     * @throws IOException
     */
    public void addBtnClick(ActionEvent actionEvent) throws IOException {
        AddCustomer.getLoggedUser(loggedUser);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/AddCustomer.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 632, 629);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends you to Edit Customer Screen
     * @param actionEvent edit button pressed
     * @throws IOException
     */
    public void editCustomer(ActionEvent actionEvent) throws IOException {
        Customer selectedCust = customerTable.getSelectionModel().getSelectedItem();

        if(selectedCust == null) {
            errorLabel.setText("Please select a Customer to Edit");
            return;
        } else {
            EditCustomer.getSelectedCustomer(selectedCust, loggedUser);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/EditCustomer.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 632, 629);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes a Customer if the requirements are met
     * @param actionEvent delete button pressed
     */
    public void deleteCustomer(ActionEvent actionEvent) {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();

        if(customer == null) {
            errorLabel.setText("Please select a Customer to Delete");
            return;
        }

        for(Appointment app : appointments) {
            if(app.getCustomerID() == customer.getCustomerID()) {
                errorLabel.setText("Appointments for " + customer.getCustomerName() + " need to be canceled before deletion.");
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Customer Deletion");
        alert.setHeaderText("Delete");
        alert.setContentText("Are you sure you want to Delete?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            CustomerDAO.deleteCustomer(customer.getCustomerID());
            customerTable.getItems().remove(customer);
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
     * Sends you to the Appointments Screen
     * @param actionEvent appointment radio selected
     * @throws IOException
     */
    public void appointmentRadioClick(ActionEvent actionEvent) throws IOException {
        if (appointmentRadio.isSelected()) {
            Appointments.getLoggedUser(loggedUser);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Appointments.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1320, 760);
            stage.setScene(scene);
            stage.show();
        }
    }
}
