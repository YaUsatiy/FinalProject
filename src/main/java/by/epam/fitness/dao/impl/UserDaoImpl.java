package by.epam.fitness.dao.impl;

import by.epam.fitness.builder.ClientBuilder;
import by.epam.fitness.dao.exception.DaoException;
import by.epam.fitness.dao.UserDao;
import by.epam.fitness.entity.User;
import by.epam.fitness.pool.ConnectionPool;
import by.epam.fitness.service.ServiceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String SQL_CHECK_USER_BY_LOGIN_PASSWORD = "SELECT name, surname, login, password, email FROM users_table WHERE login=? AND password=? AND active='1'";
    private static final String SQL_INSERT_USER = "INSERT INTO users_table (name, surname, login, password, email, hash) values (?,?,?,?,?,?)";
    private static final String SQL_FIND_USER = "SELECT email, hash, active FROM users_table WHERE email=? AND hash=? AND active='0'";
    private static final String SQL_ACTIVATE_USER = "UPDATE users_table SET active='1' WHERE email=? AND hash=?";
    private static final String SQL_IS_LOGIN_UNIQUE = "SELECT login FROM users_table WHERE login=?";
    private static final String SQL_RESTORE_USER1 = "SELECT hash FROM users_table WHERE email=? AND login=? AND active='1'";
    private static final String SQL_DEACTIVATE_AND_HASH = "UPDATE users_table SET active='0', hash=? WHERE email=? AND login=?";
    private static final String SQL_RESTORE_USER2 = "UPDATE users_table SET password=?, active='1' WHERE email=? AND login=? AND hash=? AND active='0'";
    private static final String SQL_UPDATE_USER = "UPDATE users_table SET name=?, surname=?, email=?, login=? WHERE login=?";

    @Override
    public Optional<User> checkUserByLoginPassword(String login, String newPassword) throws DaoException {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        User user = null;
        try{
            connection = ConnectionPool.INSTANCE.getConnection();
            preparedStatement = connection.prepareStatement(SQL_CHECK_USER_BY_LOGIN_PASSWORD);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, newPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ClientBuilder builder = new ClientBuilder();
                user = builder.build(resultSet);
                result = true;
            }
        } catch (SQLException | ServiceException e) {
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return result ? Optional.of(user) : Optional.empty();
    }

    private boolean isLoginUnique(String patternLogin) throws DaoException {
        boolean result = true;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.INSTANCE.getConnection();
            preparedStatement = connection.prepareStatement(SQL_IS_LOGIN_UNIQUE);
            preparedStatement.setString(1, patternLogin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = false;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return result;
    }

    @Override
    public boolean registerUser1(User user) throws DaoException {
        if (!isLoginUnique(user.getLogin())) {
            return false;
        }
        int result;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String name = user.getName();
        String surname = user.getSurname();
        String login = user.getLogin();
        String password = user.getPassword();
        String email = user.getEmail();
        String userHash = user.getUserHash();
        try {
            connection = ConnectionPool.INSTANCE.getConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_USER);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, userHash);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
        close(preparedStatement);
        close(connection);
    }
        return result != 0;
    }

    @Override
    public boolean registerUser2(String userEmail, String userHash) throws DaoException {
        int result = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.INSTANCE.getConnection();
            preparedStatement = connection.prepareStatement(SQL_FIND_USER);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setString(2, userHash);
            if (preparedStatement.execute()) {
                preparedStatement = connection.prepareStatement(SQL_ACTIVATE_USER);
                preparedStatement.setString(1, userEmail);
                preparedStatement.setString(2, userHash);
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
        close(preparedStatement);
        close(connection);
    }
        return result != 0;
    }

    @Override
    public boolean restoreUser1(String login, String userEmail, String userHash) throws DaoException {
        int result = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.INSTANCE.getConnection();
            preparedStatement = connection.prepareStatement(SQL_RESTORE_USER1);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setString(2, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                preparedStatement = connection.prepareStatement(SQL_DEACTIVATE_AND_HASH);
                preparedStatement.setString(1, userHash);
                preparedStatement.setString(2, userEmail);
                preparedStatement.setString(3, login);
                result = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return result != 0;
    }

    @Override
    public boolean restoreUser2(String email, String newPassword, String login, String userHash) throws DaoException {
        int result = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.INSTANCE.getConnection();
            preparedStatement = connection.prepareStatement(SQL_RESTORE_USER2);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, userHash);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return result != 0;
    }

    @Override
    public boolean updateUser(User user, String oldLogin) throws DaoException {
        int result = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String name = user.getName();
        String surname = user.getSurname();
        String login = user.getLogin();
        String email = user.getEmail();
        try {
            connection = ConnectionPool.INSTANCE.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, login);
            preparedStatement.setString(5, oldLogin);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return result != 0;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findEntityById(Long id) {
        return null;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public User update(User user) {
        return null;
    }
}
