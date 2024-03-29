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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import montalvo.planclock.DAO.AppointmentDAO;
import montalvo.planclock.DAO.CustomerDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Appointment;
import montalvo.planclock.Model.Customer;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is a controller for the Customers Screen
 */
public class Customers implements Initializable {
    private static User loggedUser = null;
    private String searchQuery = "";
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
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
    public RadioButton reportRadio;
    public ToggleGroup viewToggleGrp;
    public TextField customerSearchBox;

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
        customers = CustomerDAO.getAllCustomers();

        customerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPostal.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        customerCreateDate.setCellValueFactory(new PropertyValueFactory<>("CreateDate"));
        customerCreatedBy.setCellValueFactory(new PropertyValueFactory<>("CreatedBy"));
        customerLastUpdate.setCellValueFactory(new PropertyValueFactory<>("LastUpdated"));
        customerLastUpdateBy.setCellValueFactory(new PropertyValueFactory<>("LastUpdatedBy"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("DivisionID"));
        customerTable.setItems(customers);

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
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();

        if(customer == null) {
            errorLabel.setText("Please select a Customer to Delete");
            return;
        }

        for(Appointment app : appointments) {
            if(app.getCustomerID() == customer.getCustomerID()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Customer Deletion");
                alert.setHeaderText("Delete with appointments");
                alert.setContentText("You have appointments scheduled. Would you like to continue to Delete anyway?");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK) {
                    for(Appointment app1 : appointments) {
                        if (app1.getCustomerID() == customer.getCustomerID()) {
                            AppointmentDAO.deleteAppointment(app1.getAppointmentID());
                        }
                    }
                    CustomerDAO.deleteCustomer(customer.getCustomerID());
                    customerTable.getItems().remove(customer);
                }
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
     * Starts searching for customer when typing and displays on tableview
     * @param keyEvent keypress
     */
    public void custKeyTypedSearch(KeyEvent keyEvent) {
        searchQuery = customerSearchBox.getText();
        errorLabel.setText("");

        if(searchQuery.equals("")) {
            customerTable.setItems(customers);
        } else {
            if(searchQuery.matches("[0-9]+")) {
                int id = Integer.parseInt(searchQuery);
                customerTable.setItems(customers.filtered(customer -> customer.getCustomerID() == id));
            } else {
                customerTable.setItems(customers.filtered(customer -> customer.getCustomerName().contains(searchQuery)));
            }

            if(customerTable.getItems().size() == 0) {
                errorLabel.setText("No customers were found!");
            }
        }

        if(customerTable.getItems().size() < 2 && !searchQuery.equals("")) {
            customerTable.getSelectionModel().select(0);
        }
    }
}
