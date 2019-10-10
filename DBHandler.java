import model.Class;
import model.Course;
import model.User;

import java.util.List;

public class DBHandler {

    private String address;
    private String user;
    private String password;
    private String databasename;

    private void openConnection(String address, String user, String password, String databasename) {

    }

    public void deleteUserById(long id) {

    }

    public List<Course> getAllCourses() {
       return null;
    }

    public void saveUser(User user) {

    }

    public List<User> getAllUsers() {
        return null;
    }

    public List<User> getUsersByCourse(Course course) {
        return null;
    }

    public List<Class> getClassesByTheCourse(Course course) {
        return null;
    }

}
