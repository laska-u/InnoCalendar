package utils;

import model.Course;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Course.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
                sessionFactory.openSession();
            } catch (Exception e) {
                System.err.println("hibernate exception!" + e);
            }
        }
        return sessionFactory;
    }

    public static Session GetCurrentSession()
    {
        SessionFactory sessionFactory = getSessionFactory();
        return sessionFactory.getCurrentSession();
    }
}