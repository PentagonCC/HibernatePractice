package org.example.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.models.User;
import org.example.utils.SessionFactoryUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory = SessionFactoryUtil.getSessionFactory();

    @Override
    public User findById(int userId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            User user = session.find(User.class, userId);
            session.close();
            return user;
        } catch (HibernateException e) {
            if (session != null && session.isOpen()) {
                session.close();
            }
            logger.error(e);
        }
        return new User();
    }

    @Override
    public User create(User user) {
        Session session = null;
        Transaction createTransaction = null;
        try {
            session = sessionFactory.openSession();
            createTransaction = session.beginTransaction();
            session.persist(user);
            createTransaction.commit();
            session.close();
            return user;
        } catch (HibernateException e) {
            if (createTransaction != null) {
                createTransaction.rollback();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
            logger.error(e);
        }
        return new User();
    }


    @Override
    public User update(User user) {
        Session session = null;
        Transaction updateTransaction = null;
        try {
            session = sessionFactory.openSession();
            updateTransaction = session.beginTransaction();
            session.merge(user);
            updateTransaction.commit();
            session.close();
            return user;
        }catch (HibernateException e){
            if (updateTransaction != null) {
                updateTransaction.rollback();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
            logger.error(e);
        }
        return new User();
    }

    @Override
    public void delete(User user) {
        Session session = null;
        Transaction deleteTransaction = null;
        try {
            session = sessionFactory.openSession();
            deleteTransaction = session.beginTransaction();
            session.remove(user);
            deleteTransaction.commit();
            session.close();
        }catch (HibernateException e){
            if (deleteTransaction != null) {
                deleteTransaction.rollback();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
            logger.error(e);
        }
    }
}
