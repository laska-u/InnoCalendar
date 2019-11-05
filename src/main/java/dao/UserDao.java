package dao;

import model.Course;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDao {

    public User findById(long id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        User user = session.get(User.class, id);
        session.close();
        return user;
    }

    public void save(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }

    public void update(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
        session.close();
    }

    public void delete(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }

    public void subscribeUser(User user, Course course) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();

        Set<Course> courses = user.getCourses();
        courses.add(course);
        user.setCourses(courses);

//        Set<User> users = course.getUsers();
//        if (users != null) {
//            users.add(user);
//            course.setUsers(users);
////            updateCourse(course);
//        } else {
//            users = new HashSet<>();
//            users.add(user);
//            course.setUsers(users);
////            updateCourse(course);
//        }
//        update(user);

        session.update(user);
//        session.save(course);
        tx1.commit();
        session.close();
    }

    public List<User> findAll() {
        List<User> users = (List<User>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from model.User").list();
        return users;
    }
}