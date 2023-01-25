package montalvo.planclock.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import montalvo.planclock.DAO.CountryDAO;
import montalvo.planclock.DAO.CustomerDAO;
import montalvo.planclock.DAO.FirstLevelDivisionDAO;
import montalvo.planclock.Main;
import montalvo.planclock.Model.Customer;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EditCustomer implements Initializable {
    private static Customer cust = null;
    private static User loggedUser = null;
    public TextField customerIDField;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public ComboBox countryCombo;
    public ComboBox divisionCombo;
    public Label errorLabel;

    public static void getSelectedCustomer(Customer customer, User user) {
        cust = customer;
        loggedUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(CountryDAO.getAllCountries());
        divisionCombo.setItems(FirstLevelDivisionDAO.getAllDivisions());


        customerIDField.setText(Integer.toString(cust.getCustomerID()));
        nameField.setText(cust.getCustomerName());
        addressField.setText(cust.getAddress());
        postalCodeField.setText(cust.getPostalCode());
        phoneNumberField.setText(cust.getPhone());
        countryCombo.getSelectionModel().select(1);
        divisionCombo.getSelectionModel().select(cust.getDivisionID());
    }

    public void saveBtnClicked(ActionEvent actionEvent) throws IOException {
        int id = cust.getCustomerID();
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneNumberField.getText();
        LocalDateTime createDate = LocalDateTime.now();
        String createdBy = cust.getCreatedBy();
        Timestamp lastUpdated = Timestamp.valueOf(createDate);
        String lastUpdatedBy = loggedUser.getUserName();
        int divisionID = divisionCombo.getSelectionModel().getSelectedIndex() + 1;
        String error = "Exception: ";
        boolean exceptions = false;

        if(name.isBlank()) {
            exceptions = true;
            error += "Name is empty!\n";
        }
        if(address.isBlank()) {
            exceptions = true;
            error += "Address is empty!\n";
        }
        if(postalCode.isBlank()) {
            exceptions = true;
            error += "Postal Code is empty!\n";
        }
        if(phone.isBlank()) {
            exceptions = true;
            error += "Phone is empty!\n";
        }
        if(divisionCombo.getSelectionModel().getSelectedItem() == null ) {
            exceptions = true;
            error += "No State was chosen!\n";
        }

        if(exceptions) {
            errorLabel.setText(error);
            return;
        }

        Customer customer = new Customer(id, name, address, postalCode, phone, createDate, createdBy, lastUpdated,
                lastUpdatedBy, divisionID);
        CustomerDAO.updateCustomer(cust.getCustomerID(), customer);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Dashboard.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1300, 760);
        stage.setScene(scene);
        stage.show();
    }

    public void cancelBtnClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Dashboard.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1300, 760);
        stage.setScene(scene);
        stage.show();
    }
}
