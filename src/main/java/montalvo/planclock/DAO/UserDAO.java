package montalvo.planclock.DAO;

import javafx.collections.ObservableList;
import montalvo.planclock.Model.User;
import java.sql.*;

public class UserDAO {
    public static ObservableList<User> getAllUsers() {

        try{
            String sql = "SELECT * FROM user";

            PreparedStatment ps =
        } catch (SQLExcpetion e) {
            e.printStackTrace;
        }
        return list;
    }
}
