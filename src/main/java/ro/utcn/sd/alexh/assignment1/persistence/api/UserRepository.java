package ro.utcn.sd.alexh.assignment1.persistence.api;

import ro.utcn.sd.alexh.assignment1.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(int id);

    Optional<User> findByUsername(String username);

    void remove(User user);

    List<User> findAll();
}
