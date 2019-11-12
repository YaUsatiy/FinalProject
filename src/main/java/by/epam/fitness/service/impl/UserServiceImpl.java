package by.epam.fitness.service.impl;

import by.epam.fitness.dao.exception.DaoException;
import by.epam.fitness.dao.UserDao;
import by.epam.fitness.dao.impl.UserDaoImpl;
import by.epam.fitness.entity.User;
import by.epam.fitness.service.ServiceException;
import by.epam.fitness.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public Optional<User> checkUserByLoginPassword(String login, String password) throws ServiceException {
        try {
            String newPassword = DigestUtils.sha512Hex(password);
            return userDao.checkUserByLoginPassword(login, newPassword);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean registerUser1(User user) throws ServiceException {
        try {
            return userDao.registerUser1(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean registerUser2(String userEmail, String userHash) throws ServiceException {
        try {
            return userDao.registerUser2(userEmail, userHash);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean restoreUser1(String login, String userEmail, String userHash) throws ServiceException {
        try {
            return userDao.restoreUser1(login, userEmail, userHash);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean restoreUser2(String email, String password, String login, String userHash) throws ServiceException {
        String newPassword = DigestUtils.sha512Hex(password);
        try {
            return userDao.restoreUser2(email, newPassword, login, userHash);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) throws ServiceException {
        try {
            return userDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean save(User user) throws ServiceException {
        try {
            return userDao.save(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}