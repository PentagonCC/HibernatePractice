package org.example.dao;

import org.example.models.User;

public interface UserDao {

    User create(User user);

    User findById(int userId);

    User update(User user);

    void delete(User user);
}
