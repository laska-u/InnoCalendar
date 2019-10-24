package service;

import dao.CourseDao;
import dao.UserDao;
import model.Course;
import model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseService {

    private CourseDao courseDao = new CourseDao();

    public CourseService() {
    }

    public Course findCourse(long id) {
        return courseDao.findById(id);
    }

    public void saveCourse(Course course) {
        courseDao.save(course);
    }

    public void deleteCourse(Course course) {
        courseDao.delete(course);
    }

    public void updateCourse(Course course) {
        courseDao.update(course);
    }

    public List<Course> findAllCourses() {
        return courseDao.findAll();
    }

    public void addUser(User user, Course course) {
        Set<User> users = course.getUsers();
        if (users != null) {
            users.add(user);
            course.setUsers(users);
            updateCourse(course);
        } else {
            users = new HashSet<>();
            users.add(user);
            course.setUsers(users);
            updateCourse(course);
        }
    }

    public Course findAutoById(int id) {
        return courseDao.findCourseById(id);
    }
}
