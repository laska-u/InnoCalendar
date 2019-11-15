package dao;

import model.Course;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDao extends AbstractDao {

    public User findById(long id) {
        Session session = HibernateSessionFactoryUtil.GetCurrentSession();
        Transaction tx1 = session.beginTransaction();
        User user = session.get(User.class, id);
        tx1.commit();
        return user;
    }

    public void save(User user) {
        super.save(user);
    }

    public void update(User user) {
        super.update(user);
    }

    public void delete(User user) {
        super.delete(user);
    }

    public void subscribeUser(User user, Course course) {
        Session session = HibernateSessionFactoryUtil.GetCurrentSession();
        Transaction tx1 = session.beginTransaction();

        Set<Course> courses = user.getCourses();
        courses.add(course);
        user.setCourses(courses);

        session.update(user);
        tx1.commit();
        //session.close();
    }

    public List<User> findAll() {
        Session session = HibernateSessionFactoryUtil.GetCurrentSession();
        Transaction tx1 = session.beginTransaction();
        List<User> users = (List<User>)  session.createQuery("from model.User").list();
        tx1.commit();
        return users;
    }
}