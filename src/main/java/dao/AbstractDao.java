package dao;

import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

public abstract class AbstractDao {

    protected void save(Object entity) {
        Session session = HibernateSessionFactoryUtil.GetCurrentSession();
        Transaction tx1 = session.beginTransaction();
        session.save(entity);
       // session.close();
        tx1.commit();
    }

    protected void update(Object entity) {
        Session session = HibernateSessionFactoryUtil.GetCurrentSession();
        Transaction tx1 = session.beginTransaction();
        session.update(entity);
        tx1.commit();
    }

    protected void delete(Object entity) {
        Session session = HibernateSessionFactoryUtil.GetCurrentSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(entity);
        tx1.commit();
    }
}
