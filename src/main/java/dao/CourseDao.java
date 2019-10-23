package dao;

import model.Course;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class CourseDao {

    public Course findById(long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Course.class, id);
    }

    public void save(Course course) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(course);
        tx1.commit();
        session.close();
    }

    public void update(Course course) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(course);
        tx1.commit();
        session.close();
    }

    public void delete(Course course) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(course);
        tx1.commit();
        session.close();
    }

    public List<Course> findAll() {
        List<Course> courses = (List<Course>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from model.Course").list();
        return courses;
    }

    public Course findCourseById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Course.class, id);
    }

}
