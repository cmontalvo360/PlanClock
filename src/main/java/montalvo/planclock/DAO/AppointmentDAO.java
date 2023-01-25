package montalvo.planclock.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import montalvo.planclock.Model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AppointmentDAO {
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";

        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment appointment = new Appointment(id, title, description, location, type, start, end, createDate,
                        createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);
                appList.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appList;
    }

    public static void addAppointment(Appointment appointment) {
        try{
            String sql = "INSERT INTO appointments VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getEnd()));
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getCreateDate()));
            ps.setString(6, appointment.getCreatedBy());
            ps.setTimestamp(7, appointment.getLastUpdated());
            ps.setString(8, appointment.getLastUpdatedBy());
            ps.setInt(9, appointment.getCustomerID());
            ps.setInt(9, appointment.getUserID());
            ps.setInt(9, appointment.getContactID());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAppointment(int id, Appointment appointment) {
        try{
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?," +
                    " Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?," +
                    " Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getEnd()));
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getCreateDate()));
            ps.setString(6, appointment.getCreatedBy());
            ps.setTimestamp(7, appointment.getLastUpdated());
            ps.setString(8, appointment.getLastUpdatedBy());
            ps.setInt(9, appointment.getCustomerID());
            ps.setInt(9, appointment.getUserID());
            ps.setInt(9, appointment.getContactID());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAppointment(int id) {
        try{
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
