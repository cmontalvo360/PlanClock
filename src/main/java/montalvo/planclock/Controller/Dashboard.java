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
import montalvo.planclock.DAO.CustomerDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Customer;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    private static User loggedUser = null;
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

    public static void getLoggedUser(User user) {
        loggedUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public void addBtnClick(ActionEvent actionEvent) throws IOException {
        AddCustomer.getLoggedUser(loggedUser);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/AddCustomer.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

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
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteCustomer(ActionEvent actionEvent) {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();

        if(customer == null) {
            errorLabel.setText("Please select a Customer to Delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Customer");
        alert.setHeaderText("Delete");
        alert.setContentText("Are you sure you want to Delete?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            CustomerDAO.deleteCustomer(customer.getCustomerID());
            customerTable.getItems().remove(customer);
        }

    }

    public void exitApp(ActionEvent actionEvent) {
        Platform.exit();
    }

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
