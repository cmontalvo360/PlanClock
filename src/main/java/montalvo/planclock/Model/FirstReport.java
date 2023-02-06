package montalvo.planclock.Model;

import java.time.Month;

/**
 * This is a class for FirstReport
 */
public class FirstReport {
    private int month;
    private int year;
    private String type;
    private int count;

    public FirstReport(int month, int year, String type) {
        this.month = month;
        this.year = year;
        this.type = type;
        this.count = 1;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Increments the count by 1
     */
    public void incrementCount() {
        ++count;
    }

    /**
     * @return the month in a string format
     */
    public String getMonthString() {
        Month month1 = Month.of(month);
        return month1.toString();
    }
}
