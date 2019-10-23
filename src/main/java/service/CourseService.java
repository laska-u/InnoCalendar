package service;

import dao.CourseDao;
import dao.UserDao;
import model.Course;
import model.User;

import java.util.List;

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

    public Course findAutoById(int id) {
        return courseDao.findCourseById(id);
    }
}
