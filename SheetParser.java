import model.Course;

import java.util.List;

public class SheetParser {

    private int interval;
    private String link;

    public List<Course> getAllCourses(String link) {
        return null;
    }

    public int getCheckInterval() {
        return interval;
    }

    public void setCheckInterval(int checkInterval) {
        this.interval = checkInterval;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
