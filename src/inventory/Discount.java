package inventory;

import java.util.Date;

public class Discount {
    private double percentage;
    private Date startDate;
    private Date endDate;

    public Discount(double percentage, Date startDate, Date endDate) {
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // בדיקה אם ההנחה בתוקף כרגע
    public boolean isActive() {
        Date now = new Date();
        return now.after(startDate) && now.before(endDate);
    }

    public double getPercentage() {
        return percentage;
    }


}

