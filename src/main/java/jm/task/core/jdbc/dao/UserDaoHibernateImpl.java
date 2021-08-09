package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory = Util.getSession();
    private Session session;

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        String sql = "CREATE TABLE IF NOT EXISTS `mydatabase`.`user` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NOT NULL,\n" +
                "  `lastName` VARCHAR(45) NOT NULL,\n" +
                "  `age` TINYINT NOT NULL,\n" +
                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                "  PRIMARY KEY (`id`));";
        session.createSQLQuery(sql).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        String sql = "DROP TABLE IF EXISTS `mydatabase`.`user`;";
        session.createSQLQuery(sql).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(new User(name,lastName,age));
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        String sql = "DELETE FROM `mydatabase`.`user` WHERE (`id` = '" + id + "')";
        session.createSQLQuery(sql).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String sql = "FROM User";
        users = session.createQuery(sql).list();
        session.getTransaction().commit();
        session.close();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        String sql = "TRUNCATE TABLE `mydatabase`.`user`";
        session.createSQLQuery(sql).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
