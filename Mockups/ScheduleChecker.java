import model.Notification;

public class ScheduleChecker {

    private long checkInterval;

    public void checkScheduleForReminders() {
        Reminder reminder = new Reminder();
        DBHandler dbHandler = new DBHandler();
        Notification notification = new Notification();
    }

    public void checkScheduleForChanges() {
        Reminder reminder = new Reminder();
        DBHandler dbHandler = new DBHandler();
        Notification notification = new Notification();
        SheetParser parser = new SheetParser();
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

}
