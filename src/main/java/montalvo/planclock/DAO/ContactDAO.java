package montalvo.planclock.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import montalvo.planclock.Model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is a Data Access Object for Contact
 */
public class ContactDAO {
    /**
     * Gets all the Contacts from the database
     * @return
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM contacts";

        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                Contact contact = new Contact(id, name, email);
                contactList.add(contact);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }
}
