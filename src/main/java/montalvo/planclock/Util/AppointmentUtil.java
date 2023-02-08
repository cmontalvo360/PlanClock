package montalvo.planclock.Util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import montalvo.planclock.DAO.AppointmentDAO;
import montalvo.planclock.Model.Appointment;

import java.time.DayOfWeek;
import java.time.ZoneId;

/**
 * This is a Utility class for Appointments
 */
public class AppointmentUtil {
private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    /**
     * Checks if appointment has any scheduling related issue
     * @param app
     * @return a string of error otherwise empty
     */
    public static String appChecker(Appointment app) {
        appointments = AppointmentDAO.getAllAppointments();
        ZoneId easternZone = ZoneId.of("America/New_York");
        ZoneId currentZone = ZoneId.systemDefault();
        int startH = app.getStart().atZone(currentZone).withZoneSameInstant(easternZone).getHour();
        int endH = app.getEnd().atZone(currentZone).withZoneSameInstant(easternZone).getHour();
        DayOfWeek startD = app.getStart().atZone(currentZone).withZoneSameInstant(easternZone).getDayOfWeek();
        DayOfWeek endD = app.getEnd().atZone(currentZone).withZoneSameInstant(easternZone).getDayOfWeek();
        String error = "";


        if( app.getStart().isAfter(app.getEnd()) ) {
            error += "Appointment start must happen before appointment end\n";
        }

        // checking if within business hours
        if(startD == DayOfWeek.SATURDAY || startD == DayOfWeek.SUNDAY) {
            error += "-Outside of business hours,\n-8:00 a.m. to 10:00 p.m. EST, including weekends!\n";
            return error;
        }
        if(endD == DayOfWeek.SATURDAY || endD == DayOfWeek.SUNDAY) {
            error += "Outside of business hours,\n 8:00 a.m. to 10:00 p.m. EST, including weekends!\n";
            return error;
        }
        if(startH < 8 || startH > 22) {
            error += "Outside of business hours,\n 8:00 a.m. to 10:00 p.m. EST, including weekends!\n";
            return error;
        }
        if(endH < 8 || endH > 22) {
            error += "Outside of business hours,\n 8:00 a.m. to 10:00 p.m. EST, including weekends!\n";
            return error;
        }

        // checking for overlapping appointments
        for(Appointment app2: appointments) {
            if(app.getAppointmentID() == app2.getAppointmentID()) {
                continue;
            }

            if(( app.getStart().isEqual(app2.getStart()) || app.getStart().isAfter(app2.getStart()) ) && app.getStart().isBefore(app2.getEnd())) {
                error += "Appointment is overlapping with another appointment";
                return error;
            }
            if(( app.getEnd().isEqual(app2.getStart()) || app.getEnd().isAfter(app2.getStart()) ) && app.getEnd().isBefore(app2.getEnd())) {
                error += "Appointment is overlapping with another appointment";
                return error;
            }
            if(app.getStart().isBefore(app2.getStart()) && app.getEnd().isAfter(app2.getEnd())) {
                error += "Appointment is overlapping with another appointment";
                return error;
            }

        }

        return error;
    }
}
