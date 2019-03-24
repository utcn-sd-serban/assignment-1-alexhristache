package ro.utcn.sd.alexh.assignment1.persistence.memory;

import ro.utcn.sd.alexh.assignment1.entity.User;
import ro.utcn.sd.alexh.assignment1.persistence.api.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Integer, User> data = new ConcurrentHashMap<>();
    private volatile int currentId = 1;
    
    @Override
    public synchronized User save(User user) {
        if (user.getUserId() != null) {
            data.put(user.getUserId(), user);
        } else {
            user.setUserId(currentId++);
            data.put(user.getUserId(), user);
        }
        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public void remove(User user) {
        data.remove(user.getUserId());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User userToFind = null;

        for (User iteratingUser : data.values()) {
            if (iteratingUser.getUsername().equals(username)) {
                userToFind = iteratingUser;
                break;
            }
        }
        return Optional.ofNullable(userToFind);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }
}
