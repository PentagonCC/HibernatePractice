package org.example.dao;

import org.example.models.User;

public interface UserDao {

    void create(User user);

    User findById(int userId);

    void update(User user);

    void delete(User user);
}
