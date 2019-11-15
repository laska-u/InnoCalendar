package dao;

import model.Course;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class CourseDao extends  AbstractDao{

    public Course findById(long id) {
        Session session = HibernateSessionFactoryUtil.GetCurrentSession();
        Transaction tx = session.beginTransaction();
        Course course = session.get(Course.class, id);
        tx.commit();
        return course;
    }

    public void save(Course course) {
        super.save(course);
    }

    public void update(Course course) {
        super.update(course);
    }

    public void delete(Course course) {
        super.delete(course);
    }

    public List<Course> findAll() {
        Session session = HibernateSessionFactoryUtil.GetCurrentSession();
        Transaction tx = session.beginTransaction();
        List<Course> courses = (List<Course>)  session.createQuery("from model.Course").list();
        tx.commit();
        return courses;
    }

    public Course findCourseById(int id) {
        return HibernateSessionFactoryUtil.GetCurrentSession().get(Course.class, id);
    }

}
