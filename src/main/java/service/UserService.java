package service;

import dao.UserDao;
import model.Course;
import model.User;

import java.util.List;
import java.util.Set;

public class UserService {

    private UserDao usersDao = new UserDao();
    private CourseService courseService = new CourseService();

    public UserService() {
    }

    public User findUser(long id) {
        return usersDao.findById(id);
    }

    public void saveUser(User user) {
        usersDao.save(user);
    }

    public void deleteUser(User user) {
        usersDao.delete(user);
    }

    public void updateUser(User user) {
        usersDao.update(user);
    }

    public List<User> findAllUsers() {
        return usersDao.findAll();
    }

    public Course getCourseById(User user, long course_id) {

        Set<Course> courses = user.getCourses();

        if (user.getCourses() != null) {
            for (Course course : courses) {
                if (course.getId() == course_id) {
                    return course;
                }
            }
        }

        return null;
    }

    public void subscribeUser(User user, Course course) {
        usersDao.subscribeUser(user, course);
    }

    public void unSubscribeUser(User user, Course course) {
        Set<Course> courses = user.getCourses();
        courses.remove(course);
        user.setCourses(courses);
        updateUser(user);
    }




}