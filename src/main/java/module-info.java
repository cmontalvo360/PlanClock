module montalvo.planclock {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens montalvo.planclock to javafx.fxml;
    exports montalvo.planclock;
    exports montalvo.planclock.Controller;
    opens montalvo.planclock.Controller to javafx.fxml;
    exports montalvo.planclock.Model;
    opens montalvo.planclock.Model to java.base;

}