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
import javafx.scene.input.MouseEvent;
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
 * This class is a controller for the Edit Customer Screen
 */
public class EditCustomer implements Initializable {
    private static Customer cust = null;
    private static User loggedUser = null;
    private static ObservableList<Country> countries = FXCollections.observableArrayList();
    private static ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
    public TextField customerIDField;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public ComboBox<Country> countryCombo;
    public ComboBox<FirstLevelDivision> divisionCombo;
    public Label errorLabel;

    /**
     * Gets the logged-in user and selected customer from the previous screen
     * @param customer
     * @param user
     */
    public static void getSelectedCustomer(Customer customer, User user) {
        cust = customer;
        loggedUser = user;
    }

    /**
     * loads fields with data from selected customer when initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         countries = CountryDAO.getAllCountries();
         divisions = FirstLevelDivisionDAO.getAllDivisions();

        customerIDField.setText(Integer.toString(cust.getCustomerID()));
        nameField.setText(cust.getCustomerName());
        addressField.setText(cust.getAddress());
        postalCodeField.setText(cust.getPostalCode());
        phoneNumberField.setText(cust.getPhone());

        int countryId = 0;
        int divIdx = 0;

        for(FirstLevelDivision div : divisions) {
            if(div.getDivisionID() == cust.getDivisionID()) {
                countryId  = div.getCountryID();
                divIdx = divisions.indexOf(div);
            }
        }

        countryCombo.setItems(countries);
        divisionCombo.setItems(divisions);

        countryCombo.getSelectionModel().select(countryId - 1);
        divisionCombo.getSelectionModel().select(divIdx);
    }

    /**
     * Saves the Customer and sends you back to Customer Screen when requirements are met
     * @param actionEvent save button pressed
     * @throws IOException
     */
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
        int divisionID = divisionCombo.getSelectionModel().getSelectedItem().getDivisionID();
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

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/View/Customers.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1300, 760);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sends you back to the Customer Screen without saving
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
     * Filter the division combo box when a country is selected
     * Used a lambda expression to filter division list by selected country
     * @param actionEvent
     */
    public void countryFilter(ActionEvent actionEvent) {
        int countryIndex = countryCombo.getSelectionModel().getSelectedIndex() + 1;
        divisionCombo.setItems(divisions.filtered(division -> division.getCountryID() == countryIndex));
    }

    /**
     * Filter the division combo box if a country is selected
     * Used a lambda expression to filter division list by selected country
     * @param mouseEvent
     */
    public void divisionFilter(MouseEvent mouseEvent) {
        int countryIndex = countryCombo.getSelectionModel().getSelectedIndex() + 1;
        divisionCombo.setItems(divisions.filtered(division -> division.getCountryID() == countryIndex));
    }
}
