package ro.utcn.sd.alexh.assignment1.persistence.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ro.utcn.sd.alexh.assignment1.entity.User;
import ro.utcn.sd.alexh.assignment1.persistence.api.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate template;

    @Override
    public User save(User user) {
        if (user.getUserId() != null) {
            update(user);
        } else {
            Integer id = insert(user);
            user.setUserId(id);
        }
        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        List<User> users = template.query("SELECT * FROM user WHERE user_id = ?",
                (resultSet, i) -> new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("type"),
                        resultSet.getInt("score"),
                        resultSet.getBoolean("is_banned")
                ), id
        );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public Optional<User> findByUsername(String username) {
        List<User> users = template.query("SELECT * FROM user WHERE username = ?",
                (resultSet, i) -> new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("type"),
                        resultSet.getInt("score"),
                        resultSet.getBoolean("is_banned")
                ), username
        );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public void remove(User user) {
        template.update("DELETE FROM user WHERE user_id = ?", user.getUserId());
    }

    @Override
    public List<User> findAll() {
        return template.query("SELECT * FROM user", (resultSet, i) ->
                new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("type"),
                        resultSet.getInt("score"),
                        resultSet.getBoolean("is_banned")
                )
        );
    }

    private Integer insert(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("user");
        insert.usingGeneratedKeyColumns("user_id");

        Map<String, Object> data = new HashMap<>();
        data.put("user_id", user.getUserId());
        data.put("email", user.getEmail());
        data.put("username", user.getUsername());
        data.put("password", user.getPassword());
        data.put("type", user.getType());
        data.put("score", user.getScore());
        data.put("is_banned", user.isBanned());

        return insert.executeAndReturnKey(data).intValue();
    }

    private void update(User user) {
        template.update("UPDATE user SET email = ?, username = ?, password = ?, type = ?, score = ?, is_banned = ? WHERE user_id = ?",
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getType(),
                user.getScore(),
                user.isBanned(),
                user.getUserId());
    }
}
