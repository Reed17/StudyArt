package ua.artcode.dao;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import ua.artcode.exception.AppException;
import ua.artcode.exception.AppValidationException;
import ua.artcode.exception.NoSuchUserException;
import ua.artcode.exception.UserAccountExistException;
import ua.artcode.model.common.User;
import ua.artcode.validation.UserValidator;

import java.util.List;

/**
 * Created by Maxim on 17.12.2015.
 */
public class UserDaoMongoImpl implements UserDao {
    private static final Logger LOG = Logger.getLogger(UserDaoMongoImpl.class);
    private Datastore datastore;

    public UserDaoMongoImpl(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public User addUser(User user) throws AppException {
        UserValidator validator = new UserValidator();

        validator.validate(user);
        if (isExist(user.getEmail())) {
            throw new UserAccountExistException("User with this account already exist.");
        }
        // Encryption password to MD5
        // todo extract to SecurityUtils class
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        datastore.save(user);
        LOG.info("User with email: " + user.getEmail() + " was added to data base.");
        return user;
    }

    @Override
    public User findByUserEmail(String userEmail) throws NoSuchUserException {
        User user = datastore.find(User.class, "email", userEmail).get();
        if (user == null) {
            throw new NoSuchUserException("There is no user with the email: " + userEmail);
        }
        return user;
    }

    @Override
    public boolean delete(String userEmail) throws NoSuchUserException {
        User user = datastore.find(User.class).field("email").equal(userEmail).get();
        // TODO use next code line datastore.findAndDelete(datastore.find(User.class,"email",userEmail));
        if (user != null) {
            datastore.delete(User.class, user.getEmail());
            LOG.info("User with email -  " + userEmail + " was deleted from data base.");
        }
        return false;
    }

    @Override
    public User update(String email, User newUser) throws AppException {
        User oldUser = findByUserEmail(email);
        newUser.setEmail(oldUser.getEmail());
        delete(email);
        addUser(newUser);
        return newUser;
    }

    @Override
    public List<User> getAllUser() {
        return datastore.find(User.class).asList();
    }

    @Override
    public boolean isExist(String userEmail) {
        User existUser = datastore.find(User.class).field("email").equal(userEmail).get();
        if (existUser == null) {
            return false;
        }
        return true;
    }

    @Override

    public int size() {
        return (int) datastore.getDB().getCollection("User").count();
    }

}
