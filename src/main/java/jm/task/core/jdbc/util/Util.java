package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final String URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String LOG = "root";
    private static final String PASS = "root";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private static Connection connection;
    private static SessionFactory sessionFactory;


    public static SessionFactory getSession(){
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(User.class);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", DRIVER);
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.connection.url", URL);
        configuration.setProperty("hibernate.connection.username", LOG);
        configuration.setProperty("hibernate.connection.password", PASS);

        return sessionFactory = configuration.buildSessionFactory();
    }

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, LOG, PASS);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

}
