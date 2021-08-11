package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory = Util.getSession();
    private Session session;
    private Transaction transaction;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        String sql = "CREATE TABLE IF NOT EXISTS `mydatabase`.`user` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  `lastName` VARCHAR(45) NOT NULL,\n" +
                "  `age` TINYINT NOT NULL,\n" +
                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                "  PRIMARY KEY (`id`));";
        session.createSQLQuery(sql).executeUpdate();
        commitAndRollback(transaction);
        closeSession(session);
    }

    @Override
    public void dropUsersTable() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        String sql = "DROP TABLE IF EXISTS `mydatabase`.`user`;";
        session.createSQLQuery(sql).executeUpdate();
        commitAndRollback(transaction);
        closeSession(session);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.persist(new User(name,lastName,age));
        commitAndRollback(transaction);
        closeSession(session);
    }

    @Override
    public void removeUserById(long id) {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        String sql = "DELETE FROM `mydatabase`.`user` WHERE id = ?";
        session.createSQLQuery(sql).setParameter(1,id).executeUpdate();
        commitAndRollback(transaction);
        closeSession(session);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        String sql = "FROM User";
        users = session.createQuery(sql).list();
        commitAndRollback(transaction);
        closeSession(session);
        return users;
    }

    @Override
    public void cleanUsersTable() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        String sql = "TRUNCATE TABLE `mydatabase`.`user`";
        session.createSQLQuery(sql).executeUpdate();
        commitAndRollback(transaction);
        closeSession(session);
    }

    private void commitAndRollback(Transaction transaction){
        try {
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }
    }

    private void closeSession(Session session){
        if (session != null){
            try{
                session.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
