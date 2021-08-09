package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Statement statement;
    private Savepoint savepoint;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try {
            connection = Util.getConnection();
            statement = connection.createStatement();
            savepoint = connection.setSavepoint();
            String sql = "CREATE TABLE IF NOT EXISTS `mydatabase`.`user` (\n" +
                    "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  `lastName` VARCHAR(45) NOT NULL,\n" +
                    "  `age` TINYINT NOT NULL,\n" +
                    "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                    "  PRIMARY KEY (`id`));";
            statement.executeUpdate(sql);
            connectionCR(savepoint);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(statement, connection,resultSet,preparedStatement);
        }
    }

    public void dropUsersTable() {
        try {
            connection = Util.getConnection();
            statement = connection.createStatement();
            savepoint = connection.setSavepoint();
            String sql = "DROP TABLE IF EXISTS `mydatabase`.`user`;";
            statement.executeUpdate(sql);
            connectionCR(savepoint);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(statement, connection,resultSet,preparedStatement);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            connection = Util.getConnection();
            String sql = "INSERT INTO `mydatabase`.`user` (`name`, `lastName`, `age`) VALUES ('" + name + "','" + lastName + "','" + age + "')";
            preparedStatement = connection.prepareStatement(sql);
            savepoint = connection.setSavepoint();
            preparedStatement.executeUpdate();
            connectionCR(savepoint);
            System.out.println("User с именем – " + name + "  добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(statement, connection,resultSet,preparedStatement);
        }
    }

    public void removeUserById(long id) {
        try {
            connection = Util.getConnection();
            String sql = "DELETE FROM `mydatabase`.`user` WHERE (`id` = '" + id + "')";
            preparedStatement = connection.prepareStatement(sql);
            savepoint = connection.setSavepoint();
            preparedStatement.executeUpdate();
            connectionCR(savepoint);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(statement, connection,resultSet,preparedStatement);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            connection = Util.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT * FROM `mydatabase`.`user`";
            savepoint = connection.setSavepoint();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
            connectionCR(savepoint);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(statement, connection,resultSet,preparedStatement);
        }
        return users;
    }

    public void cleanUsersTable() {
        try {
            connection = Util.getConnection();
            statement = connection.createStatement();
            String sql = "TRUNCATE TABLE `mydatabase`.`user`";
            savepoint = connection.setSavepoint();
            statement.executeUpdate(sql);
            connectionCR(savepoint);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(statement, connection,resultSet,preparedStatement);
        }
    }

    private void closeConnection(Statement statement, Connection connection,
                                 ResultSet resultSet,PreparedStatement preparedStatement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectionCR(Savepoint savepoint){
        try {
            connection.commit();
        } catch (SQLException e) {
            System.out.println("commit error");
            try {
                connection.rollback(savepoint);
            } catch (SQLException ee) {
                System.out.println("rollback error");
            }
        }
    }
}

