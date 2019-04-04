package ro.utcn.sd.alexh.assignment1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.utcn.sd.alexh.assignment1.entity.User;
import ro.utcn.sd.alexh.assignment1.exception.LoginFailedException;
import ro.utcn.sd.alexh.assignment1.exception.UserAlreadyExistsException;
import ro.utcn.sd.alexh.assignment1.exception.UserNotFoundException;
import ro.utcn.sd.alexh.assignment1.exception.UserNotLoggedException;
import ro.utcn.sd.alexh.assignment1.persistence.api.RepositoryFactory;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final RepositoryFactory repositoryFactory;
    private User loggedUser;

    @Transactional
    public void login(String username, String password) {
        Optional<User> maybeUser = repositoryFactory.createUserRepository().findByUsername(username);
        if (maybeUser.isPresent() && maybeUser.get().getPassword().equals(password)) {
            loggedUser = maybeUser.get();
        } else {
            throw new LoginFailedException();
        }
    }

    @Transactional
    public User getLoggedUser() {
        if (loggedUser != null) {
            return loggedUser;
        } else {
            throw new UserNotLoggedException();
        }
    }

    @Transactional
    public void logout() {
        loggedUser = null;
    }

    @Transactional
    public List<User> listUsers() {
        return repositoryFactory.createUserRepository().findAll();
    }

    @Transactional
    public User addUser(Integer userId, String email, String username, String password, String type, int score, boolean isBanned) {
        Optional<User> maybeUser = repositoryFactory.createUserRepository().findByUsername(username);
        if (maybeUser.isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            return repositoryFactory.createUserRepository().save(new User(userId, email, username, password, type, score, isBanned));
        }
    }

    @Transactional
    public User findUserById(Integer userId) {
        Optional<User> maybeUser = repositoryFactory.createUserRepository().findById(userId);
        return maybeUser.orElse(null);
    }
}
