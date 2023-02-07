package montalvo.planclock.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import montalvo.planclock.Model.Country;
import montalvo.planclock.Model.Customer;
import montalvo.planclock.Model.FirstLevelDivision;
import montalvo.planclock.Model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This class is the controller for the Add Customer Screen
 */
public class AddCustomer implements Initializable {
    private static User loggedUser = null;
    private static ObservableList<Country> countries = FXCollections.observableArrayList();
    private static ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public ComboBox<FirstLevelDivision> divisionCombo;
    public ComboBox<Country> countryCombo;
    public Label errorLabel;

    /**
     * Gets logged-in user from the prior screen
     * @param user
     */
    public static void getLoggedUser(User user) {
        loggedUser = user;
    }

    /**
     * loads some inputs with data on initialize
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countries = CountryDAO.getAllCountries();
        divisions = FirstLevelDivisionDAO.getAllDivisions();

        divisionCombo.setItems(divisions);
        countryCombo.setItems(countries);
    }

    /**
     * Creates a new Customer and sends you to Customers Screen if requirements are met
     * @param actionEvent add button pressed
     * @throws IOException
     */
    public void addBtnClicked(ActionEvent actionEvent) throws IOException {
        int id = CustomerDAO.getAllCustomers().size() + 1;
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneNumberField.getText();
        LocalDateTime createDate = LocalDateTime.now();
        String createdBy = loggedUser.getUserName();
        Timestamp lastUpdated = Timestamp.valueOf(createDate);
        String lastUpdatedBy = loggedUser.getUserName();
        int divisionID = 0;
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
        if(countryCombo.getSelectionModel().getSelectedItem() == null ) {
            exceptions = true;
            error += "No Country was chosen!\n";
        }
        if(divisionCombo.getSelectionModel().getSelectedItem() == null ) {
            exceptions = true;
            error += "No State was chosen!\n";
        }

        if(exceptions) {
            errorLabel.setText(error);
            return;
        }

        divisionID = divisionCombo.getSelectionModel().getSelectedItem().getDivisionID();

        Customer customer = new Customer(id, name, address, postalCode, phone, createDate, createdBy, lastUpdated,
                lastUpdatedBy, divisionID);
        CustomerDAO.addCustomer(customer);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/Customers.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1300, 760);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends you back to the Customers Screen without saving
     * @param actionEvent cancel button pressed
     * @throws IOException
     */
    public void cancelBtnClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/Customers.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1300, 760);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Filters the Division combo box when a country is chosen
     * Used a lambda expression to filter division list by currently selected country
     * @param actionEvent
     */
    public void countryFilter(ActionEvent actionEvent) {
        int countryIndex = countryCombo.getSelectionModel().getSelectedIndex() + 1;
        divisionCombo.setItems(FirstLevelDivisionDAO.getAllDivisions().filtered(division -> division.getCountryID() == countryIndex));
    }
}
