package montalvo.planclock.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import montalvo.planclock.Model.Country;
import montalvo.planclock.Model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CountryDAO {
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM countries";

        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                Country country = new Country(id, countryName, createDate, createdBy, lastUpdate, lastUpdatedBy);
                countryList.add(country);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryList;
    }
}
